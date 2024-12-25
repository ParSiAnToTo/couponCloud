package com.sparta.couponcloud.orders.response;

import com.sparta.couponcloud.orders.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponseDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private List<OrderProductResponseDto> items;
    private Long totalAmount;

}
