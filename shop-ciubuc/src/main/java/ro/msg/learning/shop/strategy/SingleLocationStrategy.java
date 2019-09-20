package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.model.ids.OrderDetailId;
import ro.msg.learning.shop.model.ids.StockId;
import ro.msg.learning.shop.repository.jpa.LocationJpaRepository;
import ro.msg.learning.shop.repository.jpa.OrderJpaRepository;
import ro.msg.learning.shop.repository.jpa.ProductJpaRepository;
import ro.msg.learning.shop.repository.jpa.StockJpaRepository;
import ro.msg.learning.shop.repository.jpa.OrderDetailJpaRepository;
import ro.msg.learning.shop.repository.jpa.CustomerJpaRepository;
import ro.msg.learning.shop.repository.jpa.AddressJpaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SingleLocationStrategy implements CreateOrderStrategy {

    private final LocationJpaRepository locationJpaRepository;
    private final StockJpaRepository stockJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderDetailJpaRepository orderDetailJpaRepository;
    private final CustomerJpaRepository customerJpaRepository;
    private final AddressJpaRepository addressJpaRepository;

    @Override
    public List<Order> createOrder(OrderCreationDTO orderCreationDTO) {

        List<Location> locations = locationJpaRepository.findAll()
                .stream()
                .filter(location -> containsAllProductsAndQuantities(location, orderCreationDTO.getProducts()))
                .collect(Collectors.toList());

        if (locations.isEmpty())
            throw new CouldNotFindLocationException("Could not find a suitable location corresponding to user needs!"); //throw exception here

        List<StrategyResultsDTO> strategyResultsDTOS = new ArrayList<>();

        orderCreationDTO.getProducts().forEach(productQuantityDTO ->
                strategyResultsDTOS.add(StrategyResultsDTO.builder()
                        .location(locations.get(0))
                        .product(productJpaRepository.getOne(productQuantityDTO.getId()))
                        .quantity(productQuantityDTO.getQuantity())
                        .build()
                )
        );

        return generateOrder(strategyResultsDTOS, orderCreationDTO);
    }

    private boolean containsAllProductsAndQuantities(Location location, List<ProductQuantityDTO> products) {

        List<Stock> stocks = stockJpaRepository.getByLocation(location);

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

    private List<Order> generateOrder(List<StrategyResultsDTO> results, OrderCreationDTO orderCreationDTO) {

        Location shippingLocation = results.get(0).getLocation();
        Customer customer = customerJpaRepository.getOne(1);
        Address address = addressJpaRepository.getOne(orderCreationDTO.getAddressId());

        Order newOrder = Order.builder()
                .id(null)
                .location(shippingLocation)
                .customer(customer)
                .createdAt(orderCreationDTO.getCreatedAt())
                .address(address)
                .build();
        newOrder = orderJpaRepository.save(newOrder);

        for (StrategyResultsDTO strategyResultsDTO : results) {

            orderDetailJpaRepository.save(OrderDetail.builder()
                    .id(new OrderDetailId(newOrder.getId(), strategyResultsDTO.getProduct().getId()))
                    .quantity(strategyResultsDTO.getQuantity())
                    .build());

            StockId stockId = new StockId(strategyResultsDTO.getProduct().getId(), strategyResultsDTO.getLocation().getId());
            Stock stock = stockJpaRepository.getOne(stockId);
            stock.setQuantity(stock.getQuantity() - strategyResultsDTO.getQuantity());
            stockJpaRepository.save(stock);
        }

        return Collections.singletonList(newOrder);
    }
}
