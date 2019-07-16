package ro.msg.learning.shop.model.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data @AllArgsConstructor @NoArgsConstructor
public class StockId implements Serializable {

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "location_id")
    private Integer locationId;
}
