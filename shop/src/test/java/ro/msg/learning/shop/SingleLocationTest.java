package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SingleLocationTest extends StrategyTest {

    @InjectMocks
    private SingleLocationStrategy singleLocationStrategy;

    @Test
    public void singleLocationTestSuccessful() {

        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 40),
                new ProductQuantityDTO(2, 40)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2018-07-07T10:19:00"))
                .addressId(4)
                .products(products)
                .build();

        List<Order> ordersReturned = singleLocationStrategy.createOrder(orderCreationDTO);

        assertThat(ordersReturned.size()).isEqualTo(1);
        assertThat(ordersReturned.get(0).getCreatedAt()).isEqualTo("2018-07-07T10:19:00");
        assertThat(ordersReturned.get(0).getLocation().getId()).isEqualTo(2);
    }

    @Test(expected = CouldNotFindLocationException.class)
    public void singleLocationTestFailed() {

        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 10),
                new ProductQuantityDTO(2, 60)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2018-07-07T10:19:00"))
                .addressId(4)
                .products(products)
                .build();

        singleLocationStrategy.createOrder(orderCreationDTO);
    }
}
