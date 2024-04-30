package ru.shop.controller;

import org.springframework.web.bind.annotation.*;
import ru.shop.model.Product;
import ru.shop.model.ProductType;
import ru.shop.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService productService) {
        this.service = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Product getAll(@PathVariable UUID id) {
        return  service.getById(id);
    }

    @PostMapping
    public void save(@RequestBody Product product) {
        service.save(product);
    }

    @GetMapping("/type/{productType}")
    public List<Product> getByProductType(@PathVariable ProductType productType) {
        return service.findByProductType(productType);
    }

}
