package com.sparta.orders.controller;

import com.sparta.orders.request.OrderRequestDto;
import com.sparta.orders.response.OrderListResponseDto;
import com.sparta.orders.service.OrdersService;
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
    public ResponseEntity<List<OrderListResponseDto>> getOrderList(@RequestHeader("X-User-Id") Long userId){
        List<OrderListResponseDto> ordersList = ordersService.getOrderList(userId);
        return ResponseEntity.ok(ordersList);
    }

    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto orderRequestDto,
                                             @RequestHeader("X-User-Id") Long userId){
        ordersService.placeOrder(orderRequestDto, userId);
        return ResponseEntity.ok("Order created successfully");
    }

//    @PostMapping("/cancel-order")
//    public ResponseEntity<String> cancelOrder(@RequestBody CancelRequestDto cancelRequestDto){
//        ordersService.cancelOrder(cancelRequestDto);
//        return ResponseEntity.ok("Order cancelled successfully");
//    }
//
//    @PostMapping("/return-order")
//    public ResponseEntity<String> returnOrder(@RequestBody ReturnRequestDto returnRequestDto){
//        ordersService.returnOrder(returnRequestDto);
//        return ResponseEntity.ok("Order returned successfully");
//    }
}
