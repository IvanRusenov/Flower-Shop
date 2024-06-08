package com.ivan.Flowers.Shop.repositories;

import com.ivan.Flowers.Shop.models.entities.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, Long> {
}
