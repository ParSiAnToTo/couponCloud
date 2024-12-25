package com.sparta.couponcloud.orders.repository;

import com.sparta.couponcloud.orders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o JOIN FETCH o.orderProducts op JOIN FETCH op.product p WHERE o.users.userId = :userId")
    List<Orders> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE orders SET order_status = :nextStatus WHERE order_status = :currentStatus LIMIT :batchSize OFFSET :offset",
            nativeQuery = true)
    int updateOrderStatus(String currentStatus, String nextStatus, int offset, int batchSize);

    @Modifying
    @Query(value = "UPDATE orders SET order_status = :nextStatus WHERE order_status = :currentStatus LIMIT :batchSize OFFSET :offset",
            nativeQuery = true)
    int updateCancelStatus(String currentStatus, String nextStatus, int offset, int batchSize);

    @Modifying
    @Query(value = "UPDATE orders SET return_status = :nextReturnStatus WHERE order_status = :orderStatus AND return_status = :currentReturnStatus " +
            "LIMIT :batchSize OFFSET :offset", nativeQuery = true)
    int updateReturnStatus(String orderStatus, String currentReturnStatus, String nextReturnStatus, int offset, int batchSize);
}
