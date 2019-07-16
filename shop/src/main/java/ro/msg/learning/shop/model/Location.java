package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.base.BaseEntity;

import javax.persistence.*;

@Entity(name = "location")
@Data
public class Location implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
