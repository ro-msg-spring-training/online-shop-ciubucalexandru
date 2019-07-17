package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.ids.OrderDetailId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_detail")
@Data
public class OrderDetail implements Serializable {

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
