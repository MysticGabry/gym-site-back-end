package org.mystic.gymsite.controllers;

import lombok.RequiredArgsConstructor;
import org.mystic.gymsite.dtos.OrderDTO;
import org.mystic.gymsite.dtos.OrderItemDTO;
import org.mystic.gymsite.entities.Order;
import org.mystic.gymsite.entities.Product;
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
    public List<OrderDTO> getMyOrders(Authentication auth) {

        User user = userService.getByUsername(auth.getName());
        List<Order> orders = orderService.getUserOrders(user);

        return orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setCreatedAt(order.getCreatedAt());
            List<OrderItemDTO> items = order.getItems().stream().map(oi -> {
                Product p = oi.getProduct();
                OrderItemDTO itemDto = new OrderItemDTO();
                itemDto.setProductId(p.getId());
                itemDto.setProductName(p.getName());
                itemDto.setProductImage(p.getImageUrl());
                itemDto.setPrice(p.getPrice());
                itemDto.setQuantity(oi.getQuantity());
                return itemDto;
            }).toList();

            dto.setItems(items);
            return dto;

        }).toList();
    }


    @GetMapping("/{id}")
    public OrderDTO getOrderById(Authentication auth, @PathVariable Long id) {
        return orderService.getUserOrderById(auth.getName(), id);
    }

}
