package ru.shop.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.Customer;
import ru.shop.repository.CustomerRepo;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class CustomerServiceTest {
    private final CustomerRepo repository = Mockito.mock();
    private final CustomerService customerService = new CustomerService(repository);

    @Test
    void shouldGetCustomer() {
        // given
        UUID customerId = UUID.randomUUID();
        Customer mockedCustomer = new Customer(customerId, "name", "phone", 10);
        when(repository.findById(customerId)).thenReturn(Optional.of(mockedCustomer));
        // when
        Customer customer = customerService.getById(customerId);
        // then

        // AssertJ
        assertThat(customer).isEqualTo(mockedCustomer);
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {
        assertThatThrownBy(
                () -> customerService.getById(UUID.randomUUID())
        ).isInstanceOf(EntityNotFoundException.class);

    }
}