package com.CS490.sakila.repository;

import com.CS490.sakila.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    // You can define custom queries here if needed, for now, we're using the default CRUD methods.
}
