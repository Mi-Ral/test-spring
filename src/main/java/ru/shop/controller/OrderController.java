package ru.shop.controller;

import org.springframework.web.bind.annotation.*;
import ru.shop.model.Order;
import ru.shop.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Order> getAllProducts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping("/add")
    public void add(UUID productId, UUID customerId, long count) {
        service.add(productId, customerId, count);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getByCustomerId(@PathVariable UUID customerId) {
        return service.findByCustomer(customerId);
    }

    @GetMapping("/customer/{customerId}/total")
    public long getCustomerTotal(@PathVariable UUID customerId) {
        return service.getTotalCustomerAmount(customerId);
    }
}
