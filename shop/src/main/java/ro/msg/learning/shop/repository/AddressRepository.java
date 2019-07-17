package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Address;


public interface AddressRepository extends JpaRepository<Address, Integer> {
}
