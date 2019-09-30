package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Revenue;

public interface RevenueJpaRepository extends JpaRepository<Revenue, Integer> {
}
