package ru.shop.repository;

import org.springframework.stereotype.Repository;
import ru.shop.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepository implements IRepository<Product> {

    private List<Product> products = new ArrayList<>();

    public Optional<Product> findById(UUID id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public void save(Product product) {
        products.add(product);
    }

    public List<Product> findAll() {
        return products;
    }

}
