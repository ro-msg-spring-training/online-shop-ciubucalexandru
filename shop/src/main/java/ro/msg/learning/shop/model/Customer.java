package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
@Data
public class Customer implements BaseEntity {

    @Id
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String username;
    private String password;

    @Column(name = "email_address")
    private String emailAddress;
}
