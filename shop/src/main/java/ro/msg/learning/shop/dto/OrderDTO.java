package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.model.Order;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Integer id;
    private Integer locationId;
    private Integer customerId;
    private LocalDateTime createdAt;
    private Integer addressId;

    public static OrderDTO toDTO(Order order) {

        return OrderDTO.builder()
                .id(order.getId())
                .locationId(order.getLocation().getId())
                .customerId(order.getCustomer().getId())
                .createdAt(order.getCreatedAt())
                .addressId(order.getAddress().getId())
                .build();
    }
}
