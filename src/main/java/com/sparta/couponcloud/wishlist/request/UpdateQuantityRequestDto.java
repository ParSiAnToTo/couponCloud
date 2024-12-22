package com.sparta.couponcloud.wishlist.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateQuantityRequestDto {
    private Long wishId;
    private int quantity;
}
