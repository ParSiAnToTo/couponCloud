package com.sparta.wishlist.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDto {
    private Long wishId;
    private Long productId;
    private int quantity;
}