package com.sparta.couponcloud.orders.repository;

import com.sparta.couponcloud.orders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o JOIN FETCH o.orderProducts op JOIN FETCH op.product p WHERE o.users.userId = :userId")
    List<Orders> findAllByUserId(@Param("userId") Long userId);
}
