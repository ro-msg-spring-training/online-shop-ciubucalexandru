package ro.msg.learning.shop.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;

public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway() {

        return Flyway.configure().dataSource("jdbc:h2:mem:shop", "sa", "smth").load();
    }
}
