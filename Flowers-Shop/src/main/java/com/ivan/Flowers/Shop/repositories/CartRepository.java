package com.ivan.Flowers.Shop.repositories;

import com.ivan.Flowers.Shop.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
