package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.RevenueDTO;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.Revenue;
import ro.msg.learning.shop.repository.jpa.LocationJpaRepository;
import ro.msg.learning.shop.repository.jpa.OrderDetailJpaRepository;
import ro.msg.learning.shop.repository.jpa.OrderJpaRepository;
import ro.msg.learning.shop.repository.jpa.RevenueJpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RevenueService {

    private final LocationJpaRepository locationJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderDetailJpaRepository orderDetailJpaRepository;
    private final RevenueJpaRepository revenueJpaRepository;

    @Scheduled(cron = "00 00 00 * * *")
    public void aggregateRevenue() {
        List<Location> allLocations = locationJpaRepository.findAll();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        allLocations.forEach(location -> {
            BigDecimal sumForLocation = getSumForLocation(location);

            Revenue revenue = Revenue.builder()
                    .id(0)
                    .date(yesterday)
                    .location(location)
                    .sum(sumForLocation)
                    .build();

            revenueJpaRepository.save(revenue);
        });
    }

    private BigDecimal getSumForLocation(Location location) {
        List<Order> orders = orderJpaRepository.findAllByLocation(location);

        return orders.stream()
                .map(this::getSumForOrder)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getSumForOrder(Order order) {
        List<OrderDetail> orderDetails = orderDetailJpaRepository.getByOrder(order);

        return orderDetails.stream()
                .map(orderDetail -> orderDetail.getProduct().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<RevenueDTO> getAllRevenue() {
        return revenueJpaRepository.findAll().stream()
                .map(RevenueDTO::fromRevenue)
                .collect(Collectors.toList());
    }
}
