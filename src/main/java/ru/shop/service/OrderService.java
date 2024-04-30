package ru.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shop.exception.BadOrderCountException;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.Customer;
import ru.shop.model.Order;
import ru.shop.model.Product;
import ru.shop.repository.OrderRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo repository;
    private final CustomerService customerService;
    private final ProductService productService;

    public void add(UUID productId, UUID customerId, long count) {
        Product product = productService.getById(productId);
        Customer customer = customerService.getById(customerId);
        if (count <= 0) {
            throw new BadOrderCountException();
        }
        Order order =  new Order(UUID.randomUUID(), customer.getId(), product.getId(), count, product.getCost() * count);
        repository.save(order);
    }

    public List<Order> findByCustomer(UUID customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : repository.findAll()) {
            if (order.getCustomerId() == customerId) {
                result.add(order);
            }
        }
        return result;
    }

    public long getTotalCustomerAmount(UUID customerId) {
        long result = 0;
        for (Order order : findByCustomer(customerId)) {
            result += order.getAmount();
        }
        return result;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order getById(UUID id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
