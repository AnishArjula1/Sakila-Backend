package com.CS490.sakila.repository;

import com.CS490.sakila.model.Rental;
import com.CS490.sakila.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    @Query("SELECT r FROM Rental r WHERE r.inventory.film.filmId = :filmId AND r.returnDate IS NULL")
    List<Rental> findActiveRentalsByFilmId(int filmId);

    @Query("SELECT i FROM Inventory i WHERE i.film.filmId = :filmId AND i.inventoryId NOT IN (SELECT r.inventory.inventoryId FROM Rental r WHERE r.returnDate IS NULL)")
    List<Inventory> findAvailableInventoryByFilmId(int filmId);
}
