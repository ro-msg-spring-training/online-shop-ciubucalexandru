package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {

    private Integer productId;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private Double weight;
    private String imageUrl;
    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;
    private Integer supplierId;
    private String supplierName;
}
