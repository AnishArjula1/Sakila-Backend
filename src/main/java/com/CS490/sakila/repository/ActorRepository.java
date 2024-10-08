package com.CS490.sakila.repository;

import com.CS490.sakila.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

    List<Actor> findTop5ByOrderByActorIdDesc(); // Find top 5 actors

    // Custom query to get top 5 rented films for a given actor
    @Query(value = "SELECT f.film_id, f.title, COUNT(r.rental_id) AS rentals_count " +
            "FROM film f " +
            "JOIN film_actor fa ON f.film_id = fa.film_id " +
            "JOIN inventory i ON f.film_id = i.film_id " +
            "JOIN rental r ON i.inventory_id = r.inventory_id " +
            "WHERE fa.actor_id = :actorId " +
            "GROUP BY f.film_id " +
            "ORDER BY rentals_count DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5RentedFilmsByActor(@Param("actorId") int actorId);
}

