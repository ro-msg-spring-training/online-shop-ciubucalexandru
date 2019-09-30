package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Customer;

public interface CustomerJpaRepository extends JpaRepository<Customer, Integer> {
    Customer findByUsername(String username);
}
