package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.ids.OrderDetailId;

import javax.persistence.*;

@Entity
@Table(name = "order_detail")
@Data
public class OrderDetail {

    @EmbeddedId
    private OrderDetailId id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id",
        insertable = false,
        updatable = false
    )
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id",
        insertable = false,
        updatable = false
    )
    private Product product;
}
