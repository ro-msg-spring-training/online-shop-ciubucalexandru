package ro.msg.learning.shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "route.matrix")
public class RouteMatrixConfig {
    private String uri;
    private String key;
}
