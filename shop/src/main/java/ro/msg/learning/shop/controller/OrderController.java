package ro.msg.learning.shop.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Transactional
    @PostMapping("/")
    public List<OrderDTO> createOrder(@RequestBody OrderCreationDTO orderCreationDTO) {
        return orderService.createOrder(orderCreationDTO);
    }

    @GetMapping("/")
    public String testOrder() {
        return "TESTING THE SECURITY";
    }
}
