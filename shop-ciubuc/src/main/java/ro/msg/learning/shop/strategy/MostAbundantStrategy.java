package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class MostAbundantStrategy implements CreateOrderStrategy {

    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final MultipleLocationsOrder multipleLocationsOrder;

    @Override
    public List<Order> createOrder(OrderCreationDTO orderCreationDTO) {

        List<Location> locations = locationRepository.findAll();

        List<ProductQuantityDTO> products = orderCreationDTO.getProducts();

        List<Location> maxLocations = products.stream()
                .map(product -> getMaxLocation(locations, product.getId(), product.getQuantity()))
                .collect(Collectors.toList());

        if (!maxLocations.stream().allMatch(Objects::nonNull))
            throw new CouldNotFindLocationException("Could not find a suitable location corresponding to user needs!");

        List<StrategyResultsDTO> results = IntStream
                .range(0, products.size())
                .boxed()
                .map(i -> StrategyResultsDTO.builder()
                        .product(productRepository.getOne(products.get(i).getId()))
                        .location(maxLocations.get(i))
                        .quantity(products.get(i).getQuantity())
                        .build()
                )
                .collect(Collectors.toList());

        return multipleLocationsOrder.generateOrder(results, orderCreationDTO);
    }

    private Location getMaxLocation(List<Location> locations, Integer productId, Integer neededQuantity) {

        Optional<Location> optional = locations.stream()
                .filter(location -> {
                    Integer quantityOfLocation = getQuantityOfLocation(location, productId);
                    return quantityOfLocation > 0 && quantityOfLocation >= neededQuantity;
                })
                .max(Comparator.comparing(a -> getQuantityOfLocation(a, productId)));

        return optional.orElse(null);
    }

    private Integer getQuantityOfLocation(Location location, Integer productId) {

        List<Stock> stocks = stockRepository.getByLocation(location);

        Optional<Integer> optional = stocks.stream()
                .filter(stock -> stock.getProduct().getId().equals(productId))
                .map(Stock::getQuantity)
                .findFirst();

        return optional.orElse(0);
    }
}
