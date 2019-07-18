package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;

import java.util.List;

public interface CreateOrderStrategy {

    List<StrategyResultsDTO> createOrder(OrderCreationDTO orderCreationDTO);
}
