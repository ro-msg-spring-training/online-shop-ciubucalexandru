package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.RevenueDTO;
import ro.msg.learning.shop.service.RevenueService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/revenue")
    public List<RevenueDTO> getRevenue() {
        return revenueService.getAllRevenue();
    }
}
