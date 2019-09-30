package ro.msg.learning.shop.dto;

import lombok.Builder;
import lombok.Data;
import ro.msg.learning.shop.model.Stock;

@Data
@Builder
public class StockCsvDTO {

    private Integer productId;
    private Integer locationId;
    private Integer quantity;
    private String productName;
    private String locationName;

    public static StockCsvDTO fromStock(Stock stock) {
        return StockCsvDTO.builder()
                .productId(stock.getId().getProductId())
                .locationId(stock.getId().getLocationId())
                .quantity(stock.getQuantity())
                .productName(stock.getProduct().getName())
                .locationName(stock.getLocation().getName())
                .build();
    }
}
