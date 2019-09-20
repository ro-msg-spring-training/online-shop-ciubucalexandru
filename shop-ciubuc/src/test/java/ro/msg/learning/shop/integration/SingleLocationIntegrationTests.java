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
public class SingleLocationIntegrationTests extends IntegrationTests {

    @Test
    public void singleCreateSuccess() {

        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 40),
                new ProductQuantityDTO(2, 10)
        );
        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2019-07-07T12:12:12"))
                .products(products)
                .addressId(1)
                .build();

        List<Order> orders = singleLocationStrategy.createOrder(orderCreationDTO);
        Order order = orders.get(0);

        assertThat(order).isNotNull();
        assertThat(order.getAddress().getId()).isEqualTo(1);

        List<OrderDetail> details = orderDetailJpaRepository.getByOrder(orders.get(0));

        assertThat(details.get(0).getQuantity()).isEqualTo(40);
        assertThat(details.get(0).getId().getProductId()).isEqualTo(1);
        assertThat(details.get(0).getId().getOrderId()).isEqualTo(order.getId());

        assertThat(details.get(1).getQuantity()).isEqualTo(10);
        assertThat(details.get(1).getId().getProductId()).isEqualTo(2);
        assertThat(details.get(1).getId().getOrderId()).isEqualTo(order.getId());
    }

    @Test(expected = CouldNotFindLocationException.class)
    public void singleCreateFail() {
        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 40),
                new ProductQuantityDTO(2, 20)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2019-07-07T12:12:12"))
                .products(products)
                .addressId(1)
                .build();

        singleLocationStrategy.createOrder(orderCreationDTO);
    }
}
