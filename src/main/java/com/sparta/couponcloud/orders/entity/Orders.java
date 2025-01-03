package com.sparta.couponcloud.orders.entity;

import com.sparta.couponcloud.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_status")
    private ReturnStatus returnStatus;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @PrePersist
    protected void onPersist() {
        this.orderDate = LocalDateTime.now();
    }

    public Orders(Users users, OrderStatus orderStatus) {
        this.users = users;
        this.orderStatus = orderStatus;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrders(this);
    }

    public void updateOrderStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }

    public void updateReturnStatus(ReturnStatus newStatus) {
        this.returnStatus = newStatus;
    }
}
