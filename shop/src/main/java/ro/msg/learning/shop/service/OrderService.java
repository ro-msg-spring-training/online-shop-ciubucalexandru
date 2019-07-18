package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.model.ids.OrderDetailId;
import ro.msg.learning.shop.model.ids.StockId;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.strategy.CreateOrderStrategy;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CreateOrderStrategy createOrderStrategy;
    private final OrderDetailRepository orderDetailRepository;
    private final StockRepository stockRepository;
    private final CustomerRepository customerRepository;

    public OrderDTO createOrder(OrderCreationDTO orderCreationDTO) {

        List<StrategyResultsDTO> results = createOrderStrategy.createOrder(orderCreationDTO);

        Location shippingLocation = results.get(0).getLocation();
        Customer customer = customerRepository.getOne(1);

        Order newOrder = Order.builder()
                .id(null)
                .location(shippingLocation)
                .customer(customer)
                .createdAt(orderCreationDTO.getCreatedAt())
                .address(orderCreationDTO.getAddress())
                .build();
        newOrder = orderRepository.save(newOrder);

        for (StrategyResultsDTO strategyResultsDTO : results) {

            orderDetailRepository.save(OrderDetail.builder()
                    .id(new OrderDetailId(newOrder.getId(), strategyResultsDTO.getProduct().getId()))
                    .quantity(strategyResultsDTO.getQuantity())
                    .build());

            StockId stockId = new StockId(strategyResultsDTO.getProduct().getId(), strategyResultsDTO.getLocation().getId());
            Stock stock = stockRepository.getOne(stockId);
            stock.setQuantity(stock.getQuantity() - strategyResultsDTO.getQuantity());
            stockRepository.save(stock);
        }

        return OrderDTO.toDTO(newOrder);
    }
}
