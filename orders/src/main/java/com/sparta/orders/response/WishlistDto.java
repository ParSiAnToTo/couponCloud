package com.sparta.orders.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDto {
    private Long wishId;
    private Long productId;
    private int quantity;
}
