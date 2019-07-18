package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SingleLocationStrategy implements CreateOrderStrategy {

    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    @Override
    public List<StrategyResultsDTO> createOrder(OrderCreationDTO orderCreationDTO) {

        List<Location> locations = locationRepository.findAll()
                .stream()
                .filter(location -> containsAllProductsAndQuantities(location, orderCreationDTO.getProducts()))
                .collect(Collectors.toList());

        if (locations.isEmpty()) return new ArrayList<>(); //throw exception here

        List<StrategyResultsDTO> strategyResultsDTOS = new ArrayList<>();

        for (ProductQuantityDTO productQuantityDTO : orderCreationDTO.getProducts()) {
            strategyResultsDTOS.add(StrategyResultsDTO.builder()
                    .location(locations.get(0))
                    .product(productRepository.getOne(productQuantityDTO.getId()))
                    .quantity(productQuantityDTO.getQuantity())
                    .build()
            );
        }

        return strategyResultsDTOS;
    }

    private boolean containsAllProductsAndQuantities(Location location, List<ProductQuantityDTO> products) {

        List<Stock> stocks = stockRepository.getByLocation(location);

        List<Integer> stockProductsIds = stocks.stream()
                .map(stock -> stock.getId().getProductId())
                .collect(Collectors.toList());

        List<Integer> productsIds = products.stream()
                .map(ProductQuantityDTO::getId)
                .collect(Collectors.toList());

        if (!stockProductsIds.containsAll(productsIds)) return false;

        return products.stream()
                .allMatch(productQuantityDTO -> (productQuantityDTO.getQuantity() <= getStockByProductId(stocks, productQuantityDTO.getId()).getQuantity()));
    }

    private Stock getStockByProductId(List<Stock> stocks, Integer productId) {
        Optional<Stock> result = stocks.stream()
                .filter(stock -> stock.getId().getProductId().equals(productId))
                .findFirst();

        return result.orElse(null);
    }
}
