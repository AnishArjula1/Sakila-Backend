package com.CS490.sakila.controller;

import com.CS490.sakila.dto.FilmDetailsDTO;
import com.CS490.sakila.model.Customer;
import com.CS490.sakila.model.Film;
import com.CS490.sakila.model.Inventory;
import com.CS490.sakila.model.Rental;
import com.CS490.sakila.model.Staff;
import com.CS490.sakila.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class FilmController {

    private static final Logger logger = Logger.getLogger(FilmController.class.getName());

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    // Endpoint to get the top 5 films by rentals
    @GetMapping("/films/top5")
    public List<Film> getTop5Films() {
        logger.info("Fetching top 5 films by rentals");
        return filmRepository.findTop5ByRentals();
    }

    // Endpoint to get a specific film by its ID
    @GetMapping("/films/{film_id}")
    public Optional<Film> getFilmById(@PathVariable("film_id") int filmId) {
        logger.info("Fetching film with ID: " + filmId);
        return filmRepository.findById(filmId);
    }

    // Endpoint to search films by name, actor, or genre
    @GetMapping("/films/search")
    public List<Film> searchFilms(@RequestParam("query") String query, @RequestParam("type") String type) {
        logger.info("Searching films by: " + type + " with query: " + query);

        switch (type.toLowerCase()) {
            case "title":
                return filmRepository.findByTitleLike("%" + query + "%");
            case "actor":
                return filmRepository.findFilmsByActorName(query);
            case "genre":
                return filmRepository.findFilmsByGenre(query);
            default:
                throw new IllegalArgumentException("Invalid search type. Use 'title', 'actor', or 'genre'.");
        }
    }

    // Endpoint to get the details of a film including its actors and genre
    @GetMapping("/films/{film_id}/details")
    public FilmDetailsDTO getFilmDetails(@PathVariable("film_id") int filmId) {
        logger.info("Fetching film details with ID: " + filmId);

        List<Object[]> results = filmRepository.getFilmDetailsWithActorsAndGenre(filmId);
        logger.info("Number of results fetched: " + results.size());

        if (results.isEmpty()) {
            logger.warning("No data found for film ID: " + filmId);
            return null;
        }

        // Extracting film details
        String filmTitle = (String) results.get(0)[0];
        String filmDescription = (String) results.get(0)[1];
        int releaseYear = (int) results.get(0)[2];
        String genreName = (String) results.get(0)[5];  // Fix the correct index for genre

        // Extracting actors
        StringBuilder actors = new StringBuilder();
        for (Object[] result : results) {
            String actorFirstName = (String) result[3];
            String actorLastName = (String) result[4];
            actors.append(actorFirstName).append(" ").append(actorLastName).append(", ");
        }

        if (actors.length() > 0) {
            actors.setLength(actors.length() - 2);  // Remove trailing comma
        }

        return new FilmDetailsDTO(filmId, filmTitle, filmDescription, releaseYear, actors.toString(), genreName);
    }

    // New feature: Endpoint to rent a film to a customer
    @PostMapping("/films/{film_id}/rent")
    public String rentFilmToCustomer(@PathVariable("film_id") int filmId, @RequestParam("customer_id") int customerId) {
        logger.info("Attempting to rent film with ID: " + filmId + " to customer with ID: " + customerId);

        // Fetch available inventory for the film
        List<Inventory> availableInventory = rentalRepository.findAvailableInventoryByFilmId(filmId);
        if (availableInventory.isEmpty()) {
            return "No copies of the film are available for rent.";
        }

        // Fetch the customer and staff objects
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        Optional<Staff> staffOptional = staffRepository.findById(1);  

        if (customerOptional.isPresent() && staffOptional.isPresent()) {
            Inventory inventoryToRent = availableInventory.get(0);
            Customer customer = customerOptional.get();
            Staff staff = staffOptional.get();

            // Create a new rental
            Rental newRental = new Rental();
            newRental.setCustomer(customer);
            newRental.setInventory(inventoryToRent);
            newRental.setRentalDate(LocalDateTime.now());
            newRental.setStaff(staff);

            // Save the rental
            rentalRepository.save(newRental);

            logger.info("Film rented successfully.");
            return "Film rented successfully! Rental ID: " + newRental.getRentalId();
        } else {
            return "Customer or Staff not found.";
        }
    }
}








