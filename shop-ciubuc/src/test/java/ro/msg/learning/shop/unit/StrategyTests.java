package ro.msg.learning.shop.unit;

import org.junit.Before;
import org.mockito.Mock;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.model.ids.StockId;
import ro.msg.learning.shop.repository.jpa.LocationJpaRepository;
import ro.msg.learning.shop.repository.jpa.OrderJpaRepository;
import ro.msg.learning.shop.repository.jpa.ProductJpaRepository;
import ro.msg.learning.shop.repository.jpa.StockJpaRepository;
import ro.msg.learning.shop.repository.jpa.CustomerJpaRepository;
import ro.msg.learning.shop.repository.jpa.AddressJpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StrategyTests {

    @Mock
    private LocationJpaRepository locationJpaRepository;

    @Mock
    private StockJpaRepository stockJpaRepository;

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private CustomerJpaRepository customerJpaRepository;

    @Mock
    private AddressJpaRepository addressJpaRepository;

    @Before
    public void beforeTests() {

        Address address1 = Address.builder().id(1).country("Romania").city("Cluj Napoca").street("Brassai").county("Cluj").build();
        Address address2 = Address.builder().id(2).country("Romania").city("Campia Turzii").street("Teilor").county("Cluj").build();
        Address address3 = Address.builder().id(3).country("Romania").city("Turda").street("Baritiu").county("Cluj").build();
        Address address4 = Address.builder().id(4).country("Romania").city("Campia Turzii").street("Baritiu").county("Cluj").build();

        Location location1 = Location.builder().id(1).name("First deposit").address(address1).build();
        Location location2 = Location.builder().id(2).name("Second deposit").address(address2).build();
        Location location3 = Location.builder().id(3).name("Third deposit").address(address3).build();

        ProductCategory productCategory1 = ProductCategory.builder().id(1).name("Electronics").description("Devices using electricity").build();
        ProductCategory productCategory2 = ProductCategory.builder().id(2).name("Sports Articles").description("Articles used in sportive activities").build();

        Supplier supplier = Supplier.builder().id(1).name("EMAG").build();

        Product product1 = Product.builder().id(1).name("iPhone 6s").description("THE iPhone 6S").price(new BigDecimal("1600.50")).weight(0.5).productCategory(productCategory1).supplier(supplier).imageUrl("some image").build();
        Product product2 = Product.builder().id(2).name("Basketball").description("A basketball").price(new BigDecimal("100.0")).weight(0.7).productCategory(productCategory2).supplier(supplier).imageUrl("some image").build();
        Product product3 = Product.builder().id(3).name("USB Cable").description("Male to male USB Cable").price(new BigDecimal("7.0")).weight(0.1).productCategory(productCategory1).supplier(supplier).imageUrl("some image").build();

        StockId stockId1 = new StockId(1, 1);
        StockId stockId2 = new StockId(1, 2);
        StockId stockId3 = new StockId(2, 1);
        StockId stockId4 = new StockId(2, 2);
        StockId stockId5 = new StockId(2, 3);
        StockId stockId6 = new StockId(3, 3);

        Stock stock1 = Stock.builder().id(stockId1).quantity(50).product(product1).location(location1).build();
        Stock stock3 = Stock.builder().id(stockId3).quantity(15).product(product2).location(location1).build();
        Stock stock2 = Stock.builder().id(stockId2).quantity(200).product(product1).location(location2).build();
        Stock stock4 = Stock.builder().id(stockId4).quantity(50).product(product2).location(location2).build();
        Stock stock5 = Stock.builder().id(stockId5).quantity(75).product(product2).location(location3).build();
        Stock stock6 = Stock.builder().id(stockId6).quantity(150).product(product3).location(location3).build();

        Customer customer = Customer.builder().id(1).firstName("Bogdan Alexandru").lastName("Ciubuc").emailAddress("afw@y.c").username("aciubuc").password("pass").build();

        Order orderNull1 = Order.builder().id(null).address(address4).createdAt(LocalDateTime.parse("2018-07-07T10:19:00")).customer(customer).location(location2).build();
        Order orderNull2 = Order.builder().id(null).address(address4).createdAt(LocalDateTime.parse("2018-07-07T10:19:00")).customer(customer).location(location1).build();
        Order orderNull3 = Order.builder().id(null).address(address4).createdAt(LocalDateTime.parse("2018-07-07T10:19:00")).customer(customer).location(location3).build();

        Order order1 = Order.builder().id(1).address(address4).createdAt(LocalDateTime.parse("2018-07-07T10:19:00")).customer(customer).location(location2).build();
        Order order2 = Order.builder().id(2).address(address4).createdAt(LocalDateTime.parse("2018-07-07T10:19:00")).customer(customer).location(location1).build();
        Order order3 = Order.builder().id(3).address(address4).createdAt(LocalDateTime.parse("2018-07-07T10:19:00")).customer(customer).location(location3).build();

        when(locationJpaRepository.findAll()).thenReturn(Arrays.asList(location1, location2, location3));

        when(productJpaRepository.getOne(1)).thenReturn(product1);
        when(productJpaRepository.getOne(2)).thenReturn(product2);
        when(productJpaRepository.getOne(3)).thenReturn(product3);

        when(stockJpaRepository.getByLocation(location1)).thenReturn(Arrays.asList(stock1, stock3));
        when(stockJpaRepository.getByLocation(location2)).thenReturn(Arrays.asList(stock2, stock4));
        when(stockJpaRepository.getByLocation(location3)).thenReturn(Arrays.asList(stock5, stock6));

        when(addressJpaRepository.getOne(1)).thenReturn(address1);
        when(addressJpaRepository.getOne(2)).thenReturn(address2);
        when(addressJpaRepository.getOne(3)).thenReturn(address3);
        when(addressJpaRepository.getOne(4)).thenReturn(address4);

        when(customerJpaRepository.getOne(1)).thenReturn(customer);

        when(orderJpaRepository.save(orderNull1)).thenReturn(order1);
        when(orderJpaRepository.save(orderNull2)).thenReturn(order2);
        when(orderJpaRepository.save(orderNull3)).thenReturn(order3);

        when(stockJpaRepository.getOne(any())).thenReturn(stock1);
    }
}
