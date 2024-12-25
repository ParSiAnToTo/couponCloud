package com.sparta.couponcloud.orders.controller;

import com.sparta.couponcloud.orders.request.CancelRequestDto;
import com.sparta.couponcloud.orders.request.OrderRequestDto;
import com.sparta.couponcloud.orders.request.ReturnRequestDto;
import com.sparta.couponcloud.orders.response.OrderListResponseDto;
import com.sparta.couponcloud.orders.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("/my")
    public ResponseEntity<List<OrderListResponseDto>> getOrderList(){
        List<OrderListResponseDto> ordersList = ordersService.getOrderList();
        return ResponseEntity.ok(ordersList);
    }

    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto orderRequestDto){
        ordersService.placeOrder(orderRequestDto);
        return ResponseEntity.ok("Order created successfully");
    }

    @PostMapping("/cancel-order")
    public ResponseEntity<String> cancelOrder(@RequestBody CancelRequestDto cancelRequestDto){
        ordersService.cancelOrder(cancelRequestDto);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @PostMapping("/return-order")
    public ResponseEntity<String> returnOrder(@RequestBody ReturnRequestDto returnRequestDto){
        ordersService.returnOrder(returnRequestDto);
        return ResponseEntity.ok("Order returned successfully");
    }
}
