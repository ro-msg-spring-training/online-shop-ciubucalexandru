package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Supplier;

public interface SupplierJpaRepository extends JpaRepository<Supplier, Integer> {
}
