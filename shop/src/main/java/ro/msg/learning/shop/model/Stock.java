package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.ids.StockId;

import javax.persistence.*;

@Entity
@Table(name = "stock")
@Data
public class Stock {

    @EmbeddedId
    private StockId id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id",
            insertable = false,
            updatable = false
    )
    private Product product;

    @ManyToOne
    @JoinColumn(name = "location_id",
            insertable = false,
            updatable = false
    )
    private Location location;
}
