package ro.msg.learning.shop.dto.matrix.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedLocationDTO {
    private Coordinates latLng;
    private String adminArea4;
    private String adminArea5Type;
    private String adminArea5;
    private String street;
    private String adminArea1;
    private String adminArea3;
    private String type;
    private Coordinates displayLatLng;
    private Integer linkId;
    private String postalCode;
    private String sideOfStreet;
    private Boolean dragPoint;
    private String adminArea1Type;
    private String geocodeQuality;
    private String geocodeQualityCode;
    private String adminArea3Type;
}
