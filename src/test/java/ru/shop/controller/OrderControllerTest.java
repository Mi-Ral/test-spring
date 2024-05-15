package ru.shop.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import ru.shop.model.Customer;
import ru.shop.model.Order;
import ru.shop.model.Product;
import ru.shop.model.ProductType;
import ru.shop.repository.OrderRepo;
import ru.shop.service.OrderService;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    @MockBean
    OrderRepo orderRepo;

    @MockBean
    OrderService orderService;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void shouldThrowBadOrderCountExceptionTest(){
        Map<String, Object> request = new HashMap<>();
        var customer = new Customer(UUID.randomUUID(),"customer", "phone",20);
        var product = new Product(UUID.randomUUID(), "productName",150L, ProductType.GOOD);
        request.put("product",product);
        request.put("customer", customer);
        request.put("count", -1);

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(getRootUrl() + "/order/add");

        Mockito.verify(orderRepo, Mockito.never()).save(any());
    }

    @Test
    void successVerifyAddTest(){
        Map<String, Object> request = new HashMap<>();
        Customer customer = new Customer(UUID.randomUUID(),"customer", "phone",20);
        Product product = new Product(UUID.randomUUID(), "productName",150L, ProductType.GOOD);
        request.put("product",product);
        request.put("customer", customer);
        request.put("count", 4);

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(getRootUrl() + "/order/add")
                .then()
                .statusCode(200);

        verify(orderService, times(1)).add(any(), any(), any());
    }

    @Test
    void shouldGetOrdersByCustomer(){
        UUID customerId = UUID.randomUUID();

        var order1 = new Order(UUID.randomUUID(), customerId, UUID.randomUUID(), 1L, 100L);
        var order2 = new Order(UUID.randomUUID(), customerId, UUID.randomUUID(), 2L, 200L);

        List<Order> allOrders = Arrays.asList(order1, order2);

        Mockito.when(orderService.findByCustomer(customerId)).thenReturn(allOrders);

        List<Order> orders = List.of(given()
                .when()
                .get(getRootUrl() + "/order/customer/" + customerId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Order[].class));


        assertThat(orders)
                .hasSize(2);
    }

    @Test
    void shouldGetTotalAmountByCustomerTest() {
        UUID customerId = UUID.randomUUID();

        Order order1 = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 1L, 100L);
        Order order2 = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2L, 200L);

        List<Order> customerOrders = Arrays.asList(order1, order2);

        Mockito.when(orderService.getTotalCustomerAmount(customerId)).thenReturn(500L);

        long totalAmount = given()
                .when()
                .get(getRootUrl() + "/order/customer/" + customerId + "/total")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(Long.class);

        assertThat(totalAmount).isEqualTo(500L);
    }
}
