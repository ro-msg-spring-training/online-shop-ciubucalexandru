package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "product_category")
@Data
public class ProductCategory implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;
}
