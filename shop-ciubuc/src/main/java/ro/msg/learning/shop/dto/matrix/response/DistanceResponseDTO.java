package ro.msg.learning.shop.dto.matrix.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistanceResponseDTO implements Serializable {
    private Boolean allToAll;
    private List<Double> distance;
    private List<Integer> time;
    private List<DetailedLocationDTO> locations;
    private Boolean manyToOne;
    private Information info;
}
