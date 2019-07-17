package ro.msg.learning.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ro.msg.learning.shop.model.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
public class Product implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private BigDecimal price;

    private Double weight;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Supplier supplier;

    @Column(name = "image_url")
    private String imageUrl;
}
