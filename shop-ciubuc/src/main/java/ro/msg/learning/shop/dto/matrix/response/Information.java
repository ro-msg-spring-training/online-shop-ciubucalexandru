package ro.msg.learning.shop.dto.matrix.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Information {
    private Copyright copyright;
    private Integer statusCode;
    private List<String> messages;
}
