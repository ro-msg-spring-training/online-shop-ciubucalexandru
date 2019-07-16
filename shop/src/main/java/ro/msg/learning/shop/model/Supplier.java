package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "supplier")
@Data
public class Supplier implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
}
