package ro.msg.learning.shop.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.Supplier;

import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "product")
public class ProductMongo {
    @Id
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double weight;
    private ProductCategory productCategory;
    private Supplier supplier;
    private String imageUrl;
}
