package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Product;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyResultsDTO {

    private Location location;
    private Product product;
    private Integer quantity;
}
