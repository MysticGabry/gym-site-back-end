package org.mystic.gymsite.dtos;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private double price;
    private int quantity;
}
