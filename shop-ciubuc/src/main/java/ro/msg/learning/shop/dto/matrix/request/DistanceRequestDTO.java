package ro.msg.learning.shop.dto.matrix.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistanceRequestDTO {
    private List<String> locations;
    private Options options;
}
