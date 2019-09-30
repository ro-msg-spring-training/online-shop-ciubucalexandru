package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Integer> {
}
