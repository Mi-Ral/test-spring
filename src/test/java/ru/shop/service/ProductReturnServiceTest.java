package ru.shop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.shop.exception.BadProductReturnCountException;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.Order;
import ru.shop.model.ProductReturn;
import ru.shop.repository.ProductReturnRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductReturnServiceTest {

    private final ProductReturnRepository repository = Mockito.mock(ProductReturnRepository.class);
    private final ProductReturnService service = new ProductReturnService(repository);

    @DisplayName("Сервис должен создать возврат товара")
    @Test
    void shouldSaveProductReturn() {
        // given
        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2L, 200L);
        // when
        service.add(order, 2L);
        // then
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any());
    }
    @DisplayName("Сервис должен выбросить исключение, когда количество в заказе меньше, чем в возврате")
    @Test
    void shouldThrowWhenReturningCountMoreOrderCount() {
        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2L, 200L);
        assertThatThrownBy(() -> service.add(order, 3))
                .isInstanceOf(BadProductReturnCountException.class);
    }

    @DisplayName("Сервис должен вернуть все возвраты товаров")
    @Test
    void shouldGetAllProductReturns() {
        //given
        var productReturn = new ProductReturn(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), 10);
        var productReturn1 = new ProductReturn(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), 5);
        Mockito.when(repository.findAll()).thenReturn(
                List.of(productReturn, productReturn1)
        );
        // when
        List<ProductReturn> returns = service.getAll();
        //then
        Mockito.verify(repository).findAll();
        assertThat(returns).isEqualTo(List.of(productReturn, productReturn1));
    }
    @DisplayName("Сервис должен вернуть ProductReturn по id")
    @Test
    void shouldGetProductReturnById() {
        //given
        UUID id = UUID.randomUUID();
        var mockedProductReturn = new ProductReturn(id, UUID.randomUUID(), LocalDate.now(), 10);
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(mockedProductReturn));

        // when
        ProductReturn productReturn = service.getById(id);

        // then
        assertThat(productReturn).isEqualTo(mockedProductReturn);
    }
    @DisplayName("Сервис должен выбросить исключение EntityNotFoundException при обращении с несуществующим id")
    @Test
    void shouldThrowWhenProductReturnNotFound() {
        assertThatThrownBy(
                () -> service.getById(UUID.randomUUID())
        ).isInstanceOf(EntityNotFoundException.class);
    }
}
