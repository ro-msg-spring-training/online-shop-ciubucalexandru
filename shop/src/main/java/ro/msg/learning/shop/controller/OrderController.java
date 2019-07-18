package ro.msg.learning.shop.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.OrderCreationDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public OrderDTO createOrder(@RequestBody OrderCreationDTO orderCreationDTO) {
        return orderService.createOrder(orderCreationDTO);
    }
}
