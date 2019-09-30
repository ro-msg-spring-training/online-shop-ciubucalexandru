package ro.msg.learning.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.Role;

public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
}
