package org.mystic.gymsite.services;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.entities.Product;
import org.mystic.gymsite.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private static final String BASE_IMAGE_URL = "http://localhost:8080/images/";

    private Product mapProductWithFullImageUrl(Product product) {
        String url = product.getImageUrl();
        if (url != null && !url.isEmpty() && !url.startsWith("http")) {
            product.setImageUrl(BASE_IMAGE_URL + url);
        }
        return product;
    }

    public List<Product> findAll() {
        return repository.findAll().stream()
                .map(this::mapProductWithFullImageUrl)
                .collect(Collectors.toList());
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .map(this::mapProductWithFullImageUrl)
                .orElseThrow();
    }

    public Product create(Product p) {
        return repository.save(p);
    }

    public Product update(Long id, Product updated) {
        Product existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        existing.setImageUrl(updated.getImageUrl());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
