package ru.shop.controller;

import org.springframework.web.bind.annotation.*;
import ru.shop.model.Customer;
import ru.shop.service.CustomerService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService customerService) {
        this.service = customerService;
    }

    @GetMapping
    public List<Customer> getAllProducts() {
        return service.findAll();
    }

    @PostMapping
    public void save(@RequestBody Customer customer) {
        service.save(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable UUID id){
        return service.getById(id);
    }

}
