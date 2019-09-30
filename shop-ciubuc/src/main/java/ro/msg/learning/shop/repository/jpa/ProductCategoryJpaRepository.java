package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.ProductCategory;

public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategory, Integer> {
}
