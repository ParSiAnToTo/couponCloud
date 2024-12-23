package com.sparta.couponcloud.orders.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequestDto {
    private Long orderId;
}
