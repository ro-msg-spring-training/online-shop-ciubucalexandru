package ro.msg.learning.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.model.ids.StockId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
