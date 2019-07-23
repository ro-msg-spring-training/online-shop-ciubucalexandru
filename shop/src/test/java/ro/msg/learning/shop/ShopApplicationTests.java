package ro.msg.learning.shop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.model.ids.OrderDetailId;
import ro.msg.learning.shop.model.ids.StockId;
import ro.msg.learning.shop.repository.*;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopApplicationTests {

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private StockRepository stockRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderDetailRepository orderDetailRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @InjectMocks
    private SingleLocationStrategy singleLocationStrategy;

    @Before
    public void beforeTests() {

        //singleLocationStrategy = new SingleLocationStrategy(locationRepository, stockRepository, productRepository, orderRepository, orderDetailRepository, customerRepository, addressRepository);

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

        Product product1 = Product.builder()
                .id(1)
                .name("iPhone 6s")
                .description("THE iPhone 6S")
                .price(new BigDecimal(1600.50))
                .weight(0.5)
                .productCategory(productCategory1)
                .supplier(supplier)
                .imageUrl("some image")
                .build();

        Product product2 = Product.builder()
                .id(2)
                .name("Basketball")
                .description("A basketball")
                .price(new BigDecimal(100.0))
                .weight(0.7)
                .productCategory(productCategory2)
                .supplier(supplier)
                .imageUrl("some image")
                .build();

        Product product3 = Product.builder()
                .id(3)
                .name("USB Cable")
                .description("Male to male USB Cable")
                .price(new BigDecimal(7.0))
                .weight(0.1)
                .productCategory(productCategory1)
                .supplier(supplier)
                .imageUrl("some image")
                .build();

        Stock stock1 = Stock.builder().id(new StockId(1, 1)).quantity(50).product(product1).location(location1).build();
        Stock stock3 = Stock.builder().id(new StockId(2, 1)).quantity(15).product(product2).location(location1).build();

        Stock stock2 = Stock.builder().id(new StockId(1, 2)).quantity(200).product(product1).location(location2).build();
        Stock stock4 = Stock.builder().id(new StockId(2, 2)).quantity(50).product(product2).location(location2).build();

        Stock stock5 = Stock.builder().id(new StockId(2, 3)).quantity(75).product(product2).location(location3).build();
        Stock stock6 = Stock.builder().id(new StockId(3, 3)).quantity(150).product(product3).location(location3).build();

        Customer customer = Customer.builder()
                .id(1)
                .firstName("Bogdan Alexandru")
                .lastName("Ciubuc")
                .emailAddress("afw@y.c")
                .username("aciubuc")
                .password("pass")
                .build();

        Order orderNull = Order.builder()
                .id(null)
                .address(address4)
                .createdAt(LocalDateTime.parse("2018-07-07T10:19:00"))
                .customer(customer)
                .location(location2)
                .build();

        Order order1 = Order.builder()
                .id(1)
                .address(address4)
                .createdAt(LocalDateTime.parse("2018-07-07T10:19:00"))
                .customer(customer)
                .location(location2)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder().id(new OrderDetailId(1, 1)).quantity(40).order(order1).product(product1).build();
        OrderDetail orderDetail2 = OrderDetail.builder().id(new OrderDetailId(1, 2)).quantity(60).order(order1).product(product2).build();

        when(locationRepository.findAll()).thenReturn(Arrays.asList(location1, location2, location3));

        when(productRepository.getOne(1)).thenReturn(product1);
        when(productRepository.getOne(2)).thenReturn(product2);
        when(productRepository.getOne(3)).thenReturn(product3);

        when(stockRepository.getByLocation(location1)).thenReturn(Arrays.asList(stock1, stock3));
        when(stockRepository.getByLocation(location2)).thenReturn(Arrays.asList(stock2, stock4));
        when(stockRepository.getByLocation(location3)).thenReturn(Arrays.asList(stock5, stock6));

        when(addressRepository.getOne(1)).thenReturn(address1);
        when(addressRepository.getOne(2)).thenReturn(address2);
        when(addressRepository.getOne(3)).thenReturn(address3);
        when(addressRepository.getOne(4)).thenReturn(address4);

        when(customerRepository.getOne(1)).thenReturn(customer);

        when(orderRepository.save(orderNull)).thenReturn(order1);
    }

    @Test
    public void singleLocationTest() {

        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 40),
                new ProductQuantityDTO(2, 60)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2018-07-07T10:19:00"))
                .addressId(4)
                .products(products)
                .build();

        List<Order> ordersReturned = singleLocationStrategy.createOrder(orderCreationDTO);

        assertThat(ordersReturned.size()).isEqualTo(1);
        assertThat(ordersReturned.get(0).getCreatedAt()).isEqualTo("2018-07-07 10:19:00");
        assertThat(ordersReturned.get(0).getLocation().getId()).isEqualTo(2);
    }
}
