package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.strategy.CreateOrderStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CreateOrderStrategy createOrderStrategy;

    public List<OrderDTO> createOrder(OrderCreationDTO orderCreationDTO) {

        return createOrderStrategy.createOrder(orderCreationDTO)
                .stream()
                .map(OrderDTO::toDTO)
                .collect(Collectors.toList());
    }
}
