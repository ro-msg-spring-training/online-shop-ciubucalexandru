package ro.msg.learning.shop.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "product_category")
public class ProductCategoryMongo {
    @Id
    private Integer id;
    private String name;
    private String description;
}
