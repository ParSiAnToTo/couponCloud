package com.sparta.couponcloud.wishlist.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishlistDto {
    private Long wishId;
    private Long productId;
    private String productName;
    private Long price;
    private int quantity;
}
