package ro.msg.learning.shop.model;

import lombok.Data;
import ro.msg.learning.shop.model.ids.StockId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Stock implements Serializable {

    @EmbeddedId
    private StockId id;

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
