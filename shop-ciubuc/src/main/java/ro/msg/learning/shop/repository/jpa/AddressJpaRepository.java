package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Address;


public interface AddressJpaRepository extends JpaRepository<Address, Integer> {
}
