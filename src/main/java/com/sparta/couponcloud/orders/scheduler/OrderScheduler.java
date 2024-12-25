package com.sparta.couponcloud.orders.scheduler;

import com.sparta.couponcloud.orders.entity.OrderStatus;
import com.sparta.couponcloud.orders.entity.ReturnStatus;
import com.sparta.couponcloud.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrdersRepository ordersRepository;
    private static final int batchSize = 1000;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOrderStatus() {
        log.info("scheduler activate : {}", LocalDateTime.now());

        processStatusChange(OrderStatus.배송준비중, OrderStatus.배송중);
        processStatusChange(OrderStatus.배송중, OrderStatus.배송완료);
        processCancelStatusChange(OrderStatus.주문취소중, OrderStatus.취소완료);
        processReturnStatusChange(OrderStatus.배송완료, ReturnStatus.반품요청, ReturnStatus.반품완료);

        log.info("scheduler process done : {}", LocalDateTime.now());
    }

    @Transactional
    public void processStatusChange(OrderStatus currentStatus, OrderStatus nextStatus) {
        log.info("order status change process: {} -> {}", currentStatus, nextStatus);

        int page = 0;
        int updatedCount;

        do {
            updatedCount = ordersRepository.updateOrderStatus(currentStatus.name(), nextStatus.name(), page, batchSize);
            log.info("page {} : {} record update", page, updatedCount);
            page++;
        } while (updatedCount > 0);
        log.info("order status change process done : {} -> {}", currentStatus, nextStatus);
    }

    @Transactional
    public void processCancelStatusChange(OrderStatus currentStatus, OrderStatus nextStatus) {
        log.info("order cancel process: {} -> {}", currentStatus, nextStatus);

        int page = 0;
        int updatedCount;

        do {
            updatedCount = ordersRepository.updateCancelStatus(currentStatus.name(), nextStatus.name(), page, batchSize);
            log.info("page {} : {} record update", page, updatedCount);
            page++;
        } while (updatedCount > 0);
        log.info("order cancel process done : {} -> {}", currentStatus, nextStatus);
    }

    @Transactional
    public void processReturnStatusChange(OrderStatus orderStatus, ReturnStatus currentReturnStatus, ReturnStatus nextReturnStatus) {
        log.info("return status change process: {} + {} -> {}", orderStatus, currentReturnStatus, nextReturnStatus);

        int page = 0;
        int updatedCount;

        do {
            updatedCount = ordersRepository.updateReturnStatus(
                    orderStatus.name(), currentReturnStatus.name(), nextReturnStatus.name(), page, batchSize);
            log.info("page {} : {} record update", page, updatedCount);
            page++;
        } while (updatedCount > 0);
        log.info("return status change process done : {} -> {}", orderStatus, currentReturnStatus);
    }
}
