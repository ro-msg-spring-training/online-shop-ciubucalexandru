package ro.msg.learning.shop.integration;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.repository.*;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

public class IntegrationTests {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    CustomerRepository customerRepository;

    protected @Autowired
    AddressRepository addressRepository;

    protected SingleLocationStrategy singleLocationStrategy;
    protected MostAbundantStrategy mostAbundantStrategy;

    @Before
    public void initialize() {
        singleLocationStrategy = new SingleLocationStrategy(locationRepository,
                stockRepository,
                productRepository,
                orderRepository,
                orderDetailRepository,
                customerRepository,
                addressRepository);

        mostAbundantStrategy = new MostAbundantStrategy(locationRepository,
                stockRepository,
                productRepository,
                orderRepository,
                orderDetailRepository,
                customerRepository,
                addressRepository);
    }
}
