package ro.msg.learning.shop.strategy;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.StrategyResultsDTO;

import java.util.List;

@Component
public class MostAbundantStrategy implements CreateOrderStrategy {
    @Override
    public List<StrategyResultsDTO> createOrder(OrderCreationDTO orderCreationDTO) {
        System.out.println("MOST ABUNDANT");
        return null;
    }
}
