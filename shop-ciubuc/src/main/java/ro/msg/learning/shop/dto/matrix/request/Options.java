package ro.msg.learning.shop.dto.matrix.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Options {
    private Boolean allToAll;
    private Boolean manyToOne;
}
