package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.ids.OrderDetailId;

import java.util.List;

public interface OrderDetailJpaRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    List<OrderDetail> getByOrder(Order order);
}
