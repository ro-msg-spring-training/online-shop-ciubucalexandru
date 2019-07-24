package ro.msg.learning.shop.integration;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
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
