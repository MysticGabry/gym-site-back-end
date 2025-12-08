package org.mystic.gymsite.controllers;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.dtos.CheckoutItem;
import org.mystic.gymsite.entities.Order;
import org.mystic.gymsite.entities.Product;
import org.mystic.gymsite.entities.User;
import org.mystic.gymsite.services.OrderService;
import org.mystic.gymsite.services.ProductService;
import org.mystic.gymsite.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product updated) {
        return productService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            Authentication auth,
            @RequestBody List<CheckoutItem> items) {

        User user = userService.getByUsername(auth.getName());

        Map<Long, Integer> map = items.stream()
                .collect(Collectors.toMap(
                        CheckoutItem::getProductId,
                        CheckoutItem::getQuantity
                ));

        Order order = orderService.createOrder(user, map);

        return ResponseEntity.ok(Map.of(
                "message", "Ordine creato con successo",
                "orderId", String.valueOf(order.getId())
        ));
    }
}
