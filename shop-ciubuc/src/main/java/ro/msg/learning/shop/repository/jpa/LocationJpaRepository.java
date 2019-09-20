package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Location;

public interface LocationJpaRepository extends JpaRepository<Location, Integer> {
}
