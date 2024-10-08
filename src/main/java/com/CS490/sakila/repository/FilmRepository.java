package com.CS490.sakila.repository;

import com.CS490.sakila.dto.FilmDetailsDTO;
import com.CS490.sakila.model.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends CrudRepository<Film, Integer> {

    // Custom query to find the top 5 films by number of rentals
    @Query(value = "SELECT f.* FROM film f " +
            "JOIN inventory i ON f.film_id = i.film_id " +
            "JOIN rental r ON i.inventory_id = r.inventory_id " +
            "GROUP BY f.film_id ORDER BY COUNT(r.rental_id) DESC LIMIT 5", nativeQuery = true)
    List<Film> findTop5ByRentals();

    // Search films by title (assumed title column in film table)
    List<Film> findByTitleLike(String title);

    // Search films by actor (using both first name and last name)
    @Query(value = "SELECT f.* FROM film f " +
            "JOIN film_actor fa ON f.film_id = fa.film_id " +
            "JOIN actor a ON fa.actor_id = a.actor_id " +
            "WHERE CONCAT(a.first_name, ' ', a.last_name) LIKE %:actorName%", nativeQuery = true)
    List<Film> findFilmsByActorName(@Param("actorName") String actorName);

    // Search films by genre
    @Query(value = "SELECT f.* FROM film f " +
            "JOIN film_category fc ON f.film_id = fc.film_id " +
            "JOIN category c ON fc.category_id = c.category_id " +
            "WHERE c.name LIKE %:genre%", nativeQuery = true)
    List<Film> findFilmsByGenre(@Param("genre") String genre);
    
    // Custom query to get film details, including actors and genre
    @Query(value = "SELECT f.title AS film_title, f.description AS film_description, f.release_year AS release_year, " +
                   "a.first_name AS actor_first_name, a.last_name AS actor_last_name, c.name AS genre_name " +
                   "FROM film f " +
                   "JOIN film_actor fa ON f.film_id = fa.film_id " +
                   "JOIN actor a ON fa.actor_id = a.actor_id " +
                   "JOIN film_category fc ON f.film_id = fc.film_id " +
                   "JOIN category c ON fc.category_id = c.category_id " +
                   "WHERE f.film_id = :filmId", nativeQuery = true)
    List<Object[]> getFilmDetailsWithActorsAndGenre(@Param("filmId") int filmId);
}

