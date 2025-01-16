package com.sparta.orders.service;

import com.sparta.orders.request.OrderRequestDto;
import com.sparta.orders.request.ProductDto;
import com.sparta.orders.response.OrderListResponseDto;
import com.sparta.orders.response.OrderProductResponseDto;
import com.sparta.orders.entity.OrderProduct;
import com.sparta.orders.entity.OrderStatus;
import com.sparta.orders.entity.Orders;
import com.sparta.orders.repository.OrdersRepository;
import com.sparta.orders.response.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final RestTemplate restTemplate;

    @Transactional(readOnly = true)
    public List<OrderListResponseDto> getOrderList(Long userId) {
        List<Orders> ordersList = ordersRepository.findAllByUserId(userId);

        List<OrderListResponseDto> orderListResponseDtos = new ArrayList<>();

        for (Orders order : ordersList) {
            List<OrderProductResponseDto> orderProductDtos = new ArrayList<>();
            long totalAmount = 0;

            for (OrderProduct orderProduct : order.getOrderProducts()) {
                orderProductDtos.add(new OrderProductResponseDto(
                        orderProduct.getProductId(),
                        orderProduct.getProductName(),
                        orderProduct.getQuantity(),
                        orderProduct.getPrice()
                ));

                totalAmount += orderProduct.getQuantity() * orderProduct.getPrice();
            }

            orderListResponseDtos.add(new OrderListResponseDto(
                    order.getOrderId(),
                    order.getOrderDate(),
                    order.getOrderStatus(),
                    orderProductDtos,
                    totalAmount
            ));

        }

        return orderListResponseDtos;
    }

    @Transactional
    public void placeOrder(OrderRequestDto orderRequestDto, Long userId) {

        String url = "http://localhost:8080/wishlist/getlist";
        String queryParams = "?wishlistIds=" + String.join("&wishlistIds=",
                orderRequestDto.getWishlistIds().stream()
                        .map(String::valueOf)
                        .toArray(String[]::new));
        url = url + queryParams;
        WishlistDto[] wishlists = restTemplate.getForObject(url, WishlistDto[].class);

        Orders newOrder = new Orders(userId, OrderStatus.배송준비중);

        for (WishlistDto wishlist : wishlists) {
            url = "http://localhost:8080/product/order/{productId}";
            ProductDto product = restTemplate.getForObject(url, ProductDto.class, wishlist.getProductId());

            OrderProduct orderProduct = new OrderProduct(newOrder, product.getProductId(),
                    product.getProductName(), product.getPrice(), wishlist.getQuantity());
            newOrder.addOrderProduct(orderProduct);
        }

        ordersRepository.save(newOrder);

        url = "http://localhost:8080/wishlist/removeOrder";
        restTemplate.postForObject(url, orderRequestDto.getWishlistIds(), Void.class);
    }

//    @Transactional
//    public void cancelOrder(CancelRequestDto cancelRequestDto) {
//        Long userId = getAuthenticatedUserId();
//        Orders order = ordersRepository.findById(cancelRequestDto.getOrderId())
//                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//
//        if (!order.getUsers().getUserId().equals(userId)) {
//            throw new IllegalArgumentException("You can only cancel your own orders.");
//        }
//
//        if (!order.getOrderStatus().equals(OrderStatus.배송준비중)) {
//            throw new IllegalArgumentException("취소 요청이 불가능합니다.");
//        }
//
//        order.updateOrderStatus(OrderStatus.주문취소중);
//    }
//
//    @Transactional
//    public void returnOrder(ReturnRequestDto returnRequestDto) {
//        Long userId = getAuthenticatedUserId();
//        Orders order = ordersRepository.findById(returnRequestDto.getOrderId())
//                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//
//        if (!order.getUsers().getUserId().equals(userId)) {
//            throw new IllegalArgumentException("You can only return your own orders.");
//        }
//
//        if (!order.getOrderStatus().equals(OrderStatus.배송완료)) {
//            throw new IllegalArgumentException("반품 요청이 불가능합니다.");
//        }
//
//        order.updateReturnStatus(ReturnStatus.반품요청);
//    }
}





















