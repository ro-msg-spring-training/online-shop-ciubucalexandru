package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.model.Order;

import java.util.List;

public interface CreateOrderStrategy {

    List<Order> createOrder(OrderCreationDTO orderCreationDTO);
}
