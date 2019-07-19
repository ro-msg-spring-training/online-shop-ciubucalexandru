package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationDTO {

    private LocalDateTime createdAt;
    private Integer addressId;
    private List<ProductQuantityDTO> products;
}
