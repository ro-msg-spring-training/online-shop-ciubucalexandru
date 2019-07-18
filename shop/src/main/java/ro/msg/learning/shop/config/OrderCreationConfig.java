package ro.msg.learning.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ro.msg.learning.shop.strategy.CreateOrderStrategy;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

@Configuration
@PropertySource("application.properties")
public class OrderCreationConfig {

    @Value("${order.creation.strategy}")
    private String orderCreationStrategy;

    @Bean
    public CreateOrderStrategy createOrderStrategy(SingleLocationStrategy singleLocationStrategy, MostAbundantStrategy mostAbundantStrategy) {
        if (orderCreationStrategy.equals("single_location")) return singleLocationStrategy;
        else if (orderCreationStrategy.equals("most_abundant")) return mostAbundantStrategy;
        else return null;
    }
}