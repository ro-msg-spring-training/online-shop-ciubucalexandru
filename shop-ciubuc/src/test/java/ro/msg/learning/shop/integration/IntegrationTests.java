package ro.msg.learning.shop.integration;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.repository.jpa.LocationJpaRepository;
import ro.msg.learning.shop.repository.jpa.OrderJpaRepository;
import ro.msg.learning.shop.repository.jpa.ProductJpaRepository;
import ro.msg.learning.shop.repository.jpa.StockJpaRepository;
import ro.msg.learning.shop.repository.jpa.OrderDetailJpaRepository;
import ro.msg.learning.shop.repository.jpa.CustomerJpaRepository;
import ro.msg.learning.shop.repository.jpa.AddressJpaRepository;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;
import ro.msg.learning.shop.strategy.MultipleLocationsOrder;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

public class IntegrationTests {

    protected @Autowired
    LocationJpaRepository locationJpaRepository;

    protected @Autowired
    StockJpaRepository stockJpaRepository;

    protected @Autowired
    ProductJpaRepository productJpaRepository;

    protected @Autowired
    OrderJpaRepository orderJpaRepository;

    protected @Autowired
    OrderDetailJpaRepository orderDetailJpaRepository;

    protected @Autowired
    CustomerJpaRepository customerJpaRepository;

    protected @Autowired
    AddressJpaRepository addressJpaRepository;

    protected @Autowired
    MultipleLocationsOrder multipleLocationsOrder;

    protected SingleLocationStrategy singleLocationStrategy;
    protected MostAbundantStrategy mostAbundantStrategy;

    @Before
    public void initialize() {
        singleLocationStrategy = new SingleLocationStrategy(locationJpaRepository,
                stockJpaRepository,
                productJpaRepository,
                orderJpaRepository,
                orderDetailJpaRepository,
                customerJpaRepository,
                addressJpaRepository);

        mostAbundantStrategy = new MostAbundantStrategy(locationJpaRepository,
                stockJpaRepository,
                productJpaRepository,
                multipleLocationsOrder);
    }
}
