package org.mystic.gymsite.dtos;

import lombok.Data;

@Data
public class CheckoutDTO {
    private Long productId;
    private int quantity;
}
