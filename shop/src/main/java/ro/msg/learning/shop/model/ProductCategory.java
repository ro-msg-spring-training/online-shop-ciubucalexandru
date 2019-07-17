package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_category")
@Data
public class ProductCategory implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;
}
