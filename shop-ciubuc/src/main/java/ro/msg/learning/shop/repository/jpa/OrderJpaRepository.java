package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Order;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByLocation(Location location);
}
