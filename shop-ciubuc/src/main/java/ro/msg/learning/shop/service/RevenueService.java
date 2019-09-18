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
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.RevenueRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RevenueService {

    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final RevenueRepository revenueRepository;

    @Scheduled(cron = "00 00 00 * * *")
    public void aggregateRevenue() {
        List<Location> allLocations = locationRepository.findAll();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        allLocations.forEach(location -> {
            BigDecimal sumForLocation = getSumForLocation(location);

            Revenue revenue = Revenue.builder()
                    .id(0)
                    .date(yesterday)
                    .location(location)
                    .sum(sumForLocation)
                    .build();

            revenueRepository.save(revenue);
        });
    }

    private BigDecimal getSumForLocation(Location location) {
        List<Order> orders = orderRepository.findAllByLocation(location);

        return orders.stream()
                .map(this::getSumForOrder)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getSumForOrder(Order order) {
        List<OrderDetail> orderDetails = orderDetailRepository.getByOrder(order);

        return orderDetails.stream()
                .map(orderDetail -> orderDetail.getProduct().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<RevenueDTO> getAllRevenue() {
        return revenueRepository.findAll().stream()
                .map(RevenueDTO::fromRevenue)
                .collect(Collectors.toList());
    }
}
