package org.mystic.gymsite.dtos;

import lombok.Data;

@Data
public class CheckoutItem {
    private Long productId;
    private int quantity;
}
