package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.config.RouteMatrixConfig;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;
import ro.msg.learning.shop.dto.matrix.request.DistanceRequestDTO;
import ro.msg.learning.shop.dto.matrix.request.Options;
import ro.msg.learning.shop.dto.matrix.response.DistanceResponseDTO;
import ro.msg.learning.shop.exception.AddressNotFoundException;
import ro.msg.learning.shop.exception.NoDistancesFoundException;
import ro.msg.learning.shop.exception.OrderIsIncompleteException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.jpa.AddressJpaRepository;
import ro.msg.learning.shop.repository.jpa.LocationJpaRepository;
import ro.msg.learning.shop.repository.jpa.ProductJpaRepository;
import ro.msg.learning.shop.repository.jpa.StockJpaRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProximityStrategy implements CreateOrderStrategy {

    private final RouteMatrixConfig routeMatrixConfig;
    private final LocationJpaRepository locationJpaRepository;
    private final AddressJpaRepository addressJpaRepository;
    private final StockJpaRepository stockJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final MultipleLocationsOrder multipleLocationsOrder;

    @Override
    public List<Order> createOrder(OrderCreationDTO orderCreationDTO) {

        List<String> locationAddresses = new ArrayList<>();
        List<Location> locations;
        List<ProductQuantityDTO> requestedQuantities = copyInitialProductsQuantitiesList(orderCreationDTO.getProducts());

        Optional<Address> optionalAddress = addressJpaRepository.findById(orderCreationDTO.getAddressId());
        locations = locationJpaRepository.findAll();

        if (!optionalAddress.isPresent()) throw new AddressNotFoundException();

        locationAddresses.add(optionalAddress.get().getCity() + ", " + optionalAddress.get().getCounty());

        locationAddresses.addAll(locations.stream()
                .map(location -> location.getAddress().getCity() + ", " + location.getAddress().getCounty())
                .collect(Collectors.toList()));

        DistanceResponseDTO distanceResponseDTO = getDistanceData(locationAddresses);

        List<StrategyResultsDTO> strategyResultsDTOS = generateOrderInfo(locations, orderCreationDTO, distanceResponseDTO);

        verifyOrderIntegrity(strategyResultsDTOS, requestedQuantities);

        return multipleLocationsOrder.generateOrder(strategyResultsDTOS, orderCreationDTO);
    }

    private DistanceResponseDTO getDistanceData(List<String> locationNames) {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<DistanceRequestDTO> request = new HttpEntity<>(
                new DistanceRequestDTO(locationNames, new Options(false, true)));

        String routeMatrixUrl = routeMatrixConfig.getUri() + routeMatrixConfig.getKey();

        ResponseEntity<DistanceResponseDTO> response = restTemplate.exchange(routeMatrixUrl, HttpMethod.POST, request, DistanceResponseDTO.class);

        return response.getBody();
    }

    private List<StrategyResultsDTO> generateOrderInfo(List<Location> locations, OrderCreationDTO orderCreationDTO, DistanceResponseDTO distanceResponseDTO) {
        List<Double> distances = distanceResponseDTO.getDistance();
        distances.remove(0);

        List<ProductQuantityDTO> currentQuantities = orderCreationDTO.getProducts();
        List<StrategyResultsDTO> strategyResultsDTOS = new ArrayList<>();
        int locationsInitialSize = locations.size();

        for (int i = 0; i < locationsInitialSize; i++) {

            if (currentQuantitiesEmpty(currentQuantities)) break;

            int minIndex = getMinLocationIndex(distances);

            Location location = locations.get(minIndex);
            locations.remove(minIndex);
            distances.remove(minIndex);

            currentQuantities.forEach(productQuantityDTO -> {

                if (productQuantityDTO.getQuantity() > 0) {

                    Optional<Stock> stockOptional = stockJpaRepository.getByLocationAndProductId(location, productQuantityDTO.getId());
                    Optional<Product> product = productJpaRepository.findById(productQuantityDTO.getId());

                    if (stockOptional.isPresent() && product.isPresent() && stockOptional.get().getQuantity() > 0) {
                        Integer boughtQuantity;

                        if (stockOptional.get().getQuantity() >= productQuantityDTO.getQuantity()) {
                            boughtQuantity = productQuantityDTO.getQuantity();
                        } else {
                            boughtQuantity = stockOptional.get().getQuantity();
                        }

                        strategyResultsDTOS.add(StrategyResultsDTO.builder()
                                .location(location)
                                .product(product.get())
                                .quantity(boughtQuantity)
                                .build());

                        productQuantityDTO.setQuantity(productQuantityDTO.getQuantity() - boughtQuantity);
                    }
                }
            });
        }

        return strategyResultsDTOS;
    }

    private Integer getMinLocationIndex(List<Double> distances) {
        Optional<Double> minOptional = distances.stream().min(Comparator.naturalOrder());

        if (minOptional.isPresent())
            return distances.indexOf(minOptional.get());
        else throw new NoDistancesFoundException();
    }

    private Boolean currentQuantitiesEmpty(List<ProductQuantityDTO> productQuantityDTOS) {
        return productQuantityDTOS.stream().allMatch(productQuantityDTO -> productQuantityDTO.getQuantity() <= 0);
    }

    private void verifyOrderIntegrity(List<StrategyResultsDTO> strategyResultsDTOS, List<ProductQuantityDTO> productQuantityDTOS) {
        Map<Product, Integer> resultQuantities = strategyResultsDTOS.stream()
                .collect(Collectors.toMap(StrategyResultsDTO::getProduct, StrategyResultsDTO::getQuantity, Integer::sum));

        Map<Integer, Integer> requestedQuantities = productQuantityDTOS.stream()
                .collect(Collectors.toMap(ProductQuantityDTO::getId, ProductQuantityDTO::getQuantity, Integer::sum));

        requestedQuantities.forEach((key, value) -> {
            Optional<Map.Entry<Product, Integer>> wantedEntry = resultQuantities.entrySet().stream()
                    .filter(entry -> entry.getKey().getId().equals(key) && entry.getValue().equals(value))
                    .findAny();

            if (!wantedEntry.isPresent()) {
                throw new OrderIsIncompleteException();
            }
        });
    }

    private List<ProductQuantityDTO> copyInitialProductsQuantitiesList(List<ProductQuantityDTO> initialList) {
        List<ProductQuantityDTO> newList = new ArrayList<>();
        initialList.forEach(pq -> newList.add(new ProductQuantityDTO(pq.getId(), pq.getQuantity())));
        return newList;
    }
}
