package ru.shop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import ru.shop.model.Customer;
import ru.shop.repository.CustomerRepo;

import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @MockBean
    CustomerRepo customerRepository;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void shouldSaveCustomer() {
        // given
        Map<String, String> request = new HashMap<>();
        request.put("id", UUID.randomUUID().toString());
        request.put("name", "name1");
        request.put("phone", "phone");
        request.put("age", "10");

        // then
        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post(getRootUrl() + "/customer")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldGetCustomerById() {
        UUID id = UUID.fromString("bf0d2df5-92b4-438d-b106-c581e6339feb");
        Mockito.when(customerRepository.findById(id))
                .thenReturn(Optional.of((new Customer(id, "name", "+7999", 20))));

        when()
                .get(getRootUrl() + "/customer/" + id)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}