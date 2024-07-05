package com.ivan.flower_shop.orders.repositories;

import com.ivan.flower_shop.orders.models.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
