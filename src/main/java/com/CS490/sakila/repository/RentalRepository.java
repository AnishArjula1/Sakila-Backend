package com.CS490.sakila.repository;

import com.CS490.sakila.model.Rental;
import com.CS490.sakila.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    // Query to find active rentals by film ID where return date is null
    @Query("SELECT r FROM Rental r WHERE r.inventory.film.filmId = :filmId AND r.returnDate IS NULL")
    List<Rental> findActiveRentalsByFilmId(int filmId);

    // Query to find available inventory by film ID not currently rented out
    @Query("SELECT i FROM Inventory i WHERE i.film.filmId = :filmId AND i.inventoryId NOT IN (SELECT r.inventory.inventoryId FROM Rental r WHERE r.returnDate IS NULL)")
    List<Inventory> findAvailableInventoryByFilmId(int filmId);

    // New method to retrieve all rentals by customer ID
    List<Rental> findByCustomerCustomerId(int customerId);
}
