package com.sparta.couponcloud.orders.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResponseDto {
    private Long productId;
    private String productName;
    private int quantity;
    private Long price;
}
