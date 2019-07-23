package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.ProductQuantityDTO;
import ro.msg.learning.shop.exception.CouldNotFindLocationException;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MostAbundantTests extends StrategyTests {

    @InjectMocks
    private MostAbundantStrategy mostAbundantStrategy;

    @Test
    public void mostAbundantTestSuccessful() {

        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 100),
                new ProductQuantityDTO(2, 15),
                new ProductQuantityDTO(3, 50)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2018-07-07T10:19:00"))
                .addressId(4)
                .products(products)
                .build();

        List<Order> ordersReturned = mostAbundantStrategy.createOrder(orderCreationDTO);

        assertThat(ordersReturned.size()).isEqualTo(2);
        assertThat(ordersReturned.get(0).getLocation().getId()).isEqualTo(3);
        assertThat(ordersReturned.get(1).getLocation().getId()).isEqualTo(2);
    }

    @Test(expected = CouldNotFindLocationException.class)
    public void mostAbundantTestFailure() {

        List<ProductQuantityDTO> products = Arrays.asList(
                new ProductQuantityDTO(1, 100),
                new ProductQuantityDTO(3, 300)
        );

        OrderCreationDTO orderCreationDTO = OrderCreationDTO.builder()
                .createdAt(LocalDateTime.parse("2018-07-07T10:19:00"))
                .addressId(4)
                .products(products)
                .build();

        mostAbundantStrategy.createOrder(orderCreationDTO);
    }
}
