package com.sparta.couponcloud.orders.service;

import com.sparta.couponcloud.orders.entity.OrderProduct;
import com.sparta.couponcloud.orders.entity.OrderStatus;
import com.sparta.couponcloud.orders.entity.Orders;
import com.sparta.couponcloud.orders.entity.ReturnStatus;
import com.sparta.couponcloud.orders.repository.OrdersRepository;
import com.sparta.couponcloud.orders.request.CancelRequestDto;
import com.sparta.couponcloud.orders.request.OrderRequestDto;
import com.sparta.couponcloud.orders.request.ReturnRequestDto;
import com.sparta.couponcloud.orders.response.OrderListResponseDto;
import com.sparta.couponcloud.orders.response.OrderProductResponseDto;
import com.sparta.couponcloud.products.entity.Product;
import com.sparta.couponcloud.users.entity.Users;
import com.sparta.couponcloud.users.repository.UsersRepository;
import com.sparta.couponcloud.wishlist.entity.Wishlist;
import com.sparta.couponcloud.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final WishlistRepository wishlistRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<OrderListResponseDto> getOrderList() {
        Long userId = getAuthenticatedUserId();
        List<Orders> ordersList = ordersRepository.findAllByUserId(userId);

        List<OrderListResponseDto> orderListResponseDtos = new ArrayList<>();

        for (Orders order : ordersList) {
            List<OrderProductResponseDto> orderProductDtos = new ArrayList<>();
            long totalAmount = 0;

            for (OrderProduct orderProduct : order.getOrderProducts()) {
                Product product = orderProduct.getProduct();
                orderProductDtos.add(new OrderProductResponseDto(
                        product.getProductId(),
                        product.getProductName(),
                        orderProduct.getQuantity(),
                        product.getPrice()
                ));

                totalAmount += orderProduct.getQuantity() * product.getPrice();
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
    public void placeOrder(OrderRequestDto orderRequestDto) {
        Long userId = getAuthenticatedUserId();

        List<Wishlist> wishlists = wishlistRepository.findAllById(orderRequestDto.getWishlistIds());
        if (wishlists.isEmpty()) {
            throw new IllegalArgumentException("No wishlist items found for the given IDs");
        }

        for (Wishlist wishlist : wishlists) {
            if (!wishlist.getUsers().getUserId().equals(userId)) {
                throw new IllegalArgumentException("Invalid wishlist item: " + wishlist.getWishId());
            }
        }

        Orders newOrder = new Orders(getAuthenticatedUser(userId), OrderStatus.배송준비중);

        for (Wishlist wishlist : wishlists) {
            Product product = wishlist.getProduct();
            if (!product.isAvailable()) {
                throw new IllegalArgumentException("Product is not available: " + product.getProductId());
            }

            OrderProduct orderProduct = new OrderProduct(newOrder, product, wishlist.getQuantity());
            newOrder.addOrderProduct(orderProduct);
        }

        ordersRepository.save(newOrder);
        wishlistRepository.deleteAll(wishlists);
    }

    @Transactional
    public void cancelOrder(CancelRequestDto cancelRequestDto) {
        Long userId = getAuthenticatedUserId();
        Orders order = ordersRepository.findById(cancelRequestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getUsers().getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only cancel your own orders.");
        }

        if (!order.getOrderStatus().equals(OrderStatus.배송준비중)) {
            throw new IllegalArgumentException("취소 요청이 불가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.주문취소중);
    }

    @Transactional
    public void returnOrder(ReturnRequestDto returnRequestDto) {
        Long userId = getAuthenticatedUserId();
        Orders order = ordersRepository.findById(returnRequestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getUsers().getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only return your own orders.");
        }

        if (!order.getOrderStatus().equals(OrderStatus.배송완료)) {
            throw new IllegalArgumentException("반품 요청이 불가능합니다.");
        }

        order.updateReturnStatus(ReturnStatus.반품요청);
    }


    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        return (Long) authentication.getPrincipal();
    }

    public Users getAuthenticatedUser(Long userId) {
        return usersRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("Invalid user ID"));
    }
}





















