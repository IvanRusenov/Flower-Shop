package com.ivan.flower_shop.orders.repositories;

import com.ivan.flower_shop.orders.enums.StatusType;
import com.ivan.flower_shop.orders.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByUserIdOrderByIdDesc(Long userId);

    List<Order> findAllByStatus(StatusType statusType);

    List<Order> findAllByOrderByIdDesc();
}
