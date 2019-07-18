package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.model.Product;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getProductCategory().getId())
                .categoryName(product.getProductCategory().getName())
                .categoryDescription(product.getProductCategory().getDescription())
                .supplierId(product.getSupplier().getId())
                .supplierName(product.getSupplier().getName())
                .build();
    }
}
