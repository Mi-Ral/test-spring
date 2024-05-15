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

    public void add(Product product, Customer customer, Long count) {
        System.out.println("start");
        if (count < 1) {
            System.out.println("badCount");
            throw new BadOrderCountException();
        }
        System.out.println("добавился заказ");
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
