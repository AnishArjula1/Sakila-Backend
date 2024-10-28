package com.CS490.sakila.repository;

import com.CS490.sakila.model.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    // Find inventories by Film ID
    List<Inventory> findByFilmFilmId(int filmId);
}

