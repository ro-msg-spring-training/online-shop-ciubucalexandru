package ro.msg.learning.shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.strategy.CreateOrderStrategy;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;
import ro.msg.learning.shop.strategy.ProximityStrategy;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

@Data
@Configuration
@ConfigurationProperties(prefix = "order.creation")
public class OrderCreationConfig {

    private String strategy;

    @Bean
    public CreateOrderStrategy createOrderStrategy(SingleLocationStrategy singleLocationStrategy,
                                                   MostAbundantStrategy mostAbundantStrategy,
                                                   ProximityStrategy proximityStrategy) {
        if ("single_location".equals(strategy)) return singleLocationStrategy;
        else if ("most_abundant".equals(strategy)) return mostAbundantStrategy;
        else if ("proximity".equals(strategy)) return proximityStrategy;
        else return null;
    }
}
