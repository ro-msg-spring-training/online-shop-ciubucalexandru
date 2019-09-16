package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
