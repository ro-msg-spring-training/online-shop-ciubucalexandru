package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.repository.jpa.CustomerJpaRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Customer customer = customerJpaRepository.findByUsername(username);
        User.UserBuilder userBuilder;

        if (customer != null) {
            userBuilder = User.withUsername(username);
            userBuilder.password(customer.getPassword());
            if ("alexciubuc".equals(username)) {
                userBuilder.authorities("ADMIN");
                userBuilder.roles("ADMIN");
            } else {
                userBuilder.authorities("CUSTOMER");
                userBuilder.roles("CUSTOMER");
            }
        } else {
            throw new UsernameNotFoundException("User with username: " + username + " was not found!");
        }

        return userBuilder.build();
    }
}
