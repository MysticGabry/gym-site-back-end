package org.mystic.gymsite.services;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.entities.*;
import org.mystic.gymsite.repositories.OrderItemRepository;
import org.mystic.gymsite.repositories.OrderRepository;
import org.mystic.gymsite.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(User user, Map<Long, Integer> items) {

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .total(0.0)
                .build();

        order = orderRepository.save(order);

        double total = 0.0;

        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            Long productId = entry.getKey();
            int qty = entry.getValue();

            Product p = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

            if (p.getStock() < qty) {
                throw new RuntimeException("Stock insufficiente per " + p.getName());
            }

            p.setStock(p.getStock() - qty);
            productRepository.save(p);

            double lineTotal = p.getPrice() * qty;
            total += lineTotal;

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .quantity(qty)
                    .build();

            orderItemRepository.save(oi);
            order.getItems().add(oi);
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order getUserOrderById(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Accesso negato");
        }

        return order;
    }
}
