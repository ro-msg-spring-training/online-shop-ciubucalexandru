package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.model.ids.OrderDetailId;
import ro.msg.learning.shop.model.ids.StockId;
import ro.msg.learning.shop.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MultipleLocationsOrder {

    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    List<Order> generateOrder(List<StrategyResultsDTO> results, OrderCreationDTO orderCreationDTO) {

        Customer customer = customerRepository.getOne(1);
        Address address = addressRepository.getOne(orderCreationDTO.getAddressId());

        Map<Location, Map<Product, Integer>> groupedByLocation = results
                .stream()
                .collect(Collectors.groupingBy(
                        StrategyResultsDTO::getLocation,
                        Collectors.toMap(StrategyResultsDTO::getProduct, StrategyResultsDTO::getQuantity)
                ));

        List<Order> orders = new ArrayList<>();

        groupedByLocation.forEach((key, value) -> {

            Order order = Order.builder()
                    .id(null)
                    .location(key)
                    .customer(customer)
                    .createdAt(orderCreationDTO.getCreatedAt())
                    .address(address)
                    .build();

            order = orderRepository.save(order);
            final int orderId = order.getId();
            final Location location = order.getLocation();

            orders.add(order);

            value.forEach((product, quantity) -> {

                orderDetailRepository.save(OrderDetail.builder()
                        .id(new OrderDetailId(orderId, product.getId()))
                        .quantity(quantity)
                        .build()
                );

                StockId stockId = new StockId(product.getId(), location.getId());
                Stock stock = stockRepository.getOne(stockId);
                stock.setQuantity(stock.getQuantity() - quantity);
                stockRepository.save(stock);
            });
        });

        return orders;
    }
}
