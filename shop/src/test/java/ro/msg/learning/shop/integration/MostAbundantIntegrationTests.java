package ro.msg.learning.shop.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class MostAbundantIntegrationTests extends IntegrationTests {

    @Test
    public void mostAbundantCreateSuccess() {
        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 40),
                new ProductQuantityDTO(2, 10)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2019-07-07T12:12:12"))
                .products(products)
                .addressId(1)
                .build();

        List<Order> orders = mostAbundantStrategy.createOrder(orderCreationDTO);
        Order order1 = orders.get(0);
        Order order2 = orders.get(1);

        List<OrderDetail> details1 = orderDetailRepository.getByOrder(order1);
        List<OrderDetail> details2 = orderDetailRepository.getByOrder(order2);

        assertThat(details1.size()).isEqualTo(1);
        assertThat(details2.size()).isEqualTo(1);

        assertThat(details1.get(0).getQuantity()).isEqualTo(10);
        assertThat(details1.get(0).getId().getOrderId()).isEqualTo(order1.getId());
        assertThat(details1.get(0).getId().getProductId()).isEqualTo(2);

        assertThat(details2.get(0).getQuantity()).isEqualTo(40);
        assertThat(details2.get(0).getId().getOrderId()).isEqualTo(order2.getId());
        assertThat(details2.get(0).getId().getProductId()).isEqualTo(1);
    }

    @Test(expected = CouldNotFindLocationException.class)
    public void mostAbundantCreateFail() {
        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 40),
                new ProductQuantityDTO(2, 500)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2019-07-07T12:12:12"))
                .products(products)
                .addressId(1)
                .build();

        mostAbundantStrategy.createOrder(orderCreationDTO);
    }
}
