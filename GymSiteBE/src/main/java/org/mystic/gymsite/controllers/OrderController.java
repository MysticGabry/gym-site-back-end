package org.mystic.gymsite.controllers;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.entities.Order;
import org.mystic.gymsite.entities.User;
import org.mystic.gymsite.services.OrderService;
import org.mystic.gymsite.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/mine")
    public List<Order> getMyOrders(Authentication auth) {
        User user = userService.getByUsername(auth.getName());
        return orderService.getUserOrders(user);
    }

    @GetMapping("/{id}")
    public Order getOrderById(Authentication auth, @PathVariable Long id) {
        User user = userService.getByUsername(auth.getName());
        return orderService.getUserOrderById(user, id);
    }
}
