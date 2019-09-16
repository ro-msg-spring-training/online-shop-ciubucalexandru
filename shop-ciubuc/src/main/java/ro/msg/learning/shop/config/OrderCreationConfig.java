package ro.msg.learning.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.strategy.CreateOrderStrategy;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

@Configuration
public class OrderCreationConfig {

    @Value("${order.creation.strategy}")
    private String orderCreationStrategy;

    @Bean
    public CreateOrderStrategy createOrderStrategy(SingleLocationStrategy singleLocationStrategy, MostAbundantStrategy mostAbundantStrategy) {
        if ("single_location".equals(orderCreationStrategy)) return singleLocationStrategy;
        else if ("most_abundant".equals(orderCreationStrategy)) return mostAbundantStrategy;
        else return null;
    }
}
