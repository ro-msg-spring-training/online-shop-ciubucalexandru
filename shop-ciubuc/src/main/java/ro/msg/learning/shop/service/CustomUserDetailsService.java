package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Role;
import ro.msg.learning.shop.repository.jpa.CustomerJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerJpaRepository customerJpaRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {

        Customer customer = customerJpaRepository.findByUsername(username);
        User.UserBuilder userBuilder;

        if (customer != null) {
            userBuilder = User.withUsername(username);
            userBuilder.password(customer.getPassword());
            getPermissions(customer.getUserRoles()).forEach(userBuilder::authorities);
        } else {
            throw new UsernameNotFoundException("User with username: " + username + " was not found!");
        }

        return userBuilder.build();
    }

    private List<String> getPermissions(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

}
