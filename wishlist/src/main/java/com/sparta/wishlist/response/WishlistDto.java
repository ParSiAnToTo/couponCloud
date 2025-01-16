package com.sparta.wishlist.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDto {
    private Long wishId;
    private Long productId;
    private String productName;
    private Long price;
    private int quantity;
}
