package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.repository.CustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class OrderCreationIntegrationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveCustomerTest() {

        Customer customer = Customer.builder()
                .id(3)
                .firstName("Alex")
                .lastName("Ciubuc")
                .username("ciubuc")
                .password("pass")
                .emailAddress("ac@y.c")
                .build();

        customerRepository.save(customer);

        Customer retrievedCustomer = customerRepository.getOne(3);
        assertThat(retrievedCustomer.getEmailAddress()).isEqualTo(customer.getEmailAddress());
        assertThat(retrievedCustomer.getUsername()).isEqualTo(customer.getUsername());
        assertThat(retrievedCustomer.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(retrievedCustomer.getLastName()).isEqualTo(customer.getLastName());
    }
}
