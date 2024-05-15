package ru.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.shop.exception.BadOrderCountException;
import ru.shop.model.Customer;
import ru.shop.model.Product;
import ru.shop.model.ProductType;
import ru.shop.repository.OrderRepo;

import java.util.UUID;

public class OrderServiceTest {
    private final OrderRepo orderRepository = Mockito.mock();
    private final OrderService orderService = new OrderService(orderRepository);

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void shouldThrowBadOrderCountException(Integer count) {
        // given
        var customer = new Customer(UUID.randomUUID(), "customerName", "customerPhone", 11);
        var product = new Product(UUID.randomUUID(), "productName", 10, ProductType.GOOD);

        // then
        Assertions.assertThrows(
                BadOrderCountException.class,
                () -> orderService.add(product, customer, Long.valueOf(count))
        );
    }
}
