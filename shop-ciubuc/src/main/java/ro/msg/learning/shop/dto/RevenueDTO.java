package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Revenue;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDTO {
    private Integer id;
    private Integer locationId;
    private String locationName;
    private String locationAddress;
    private String date;
    private BigDecimal sum;

    public static RevenueDTO fromRevenue(Revenue revenue) {

        Address address = revenue.getLocation().getAddress();

        return RevenueDTO.builder()
                .id(revenue.getId())
                .locationId(revenue.getLocation().getId())
                .locationName(revenue.getLocation().getName())
                .locationAddress(address.getStreet() + ", " + address.getCity() + ", " + address.getCounty() + ", " + address.getCountry())
                .date(revenue.getDate().toString())
                .sum(revenue.getSum())
                .build();
    }
}
