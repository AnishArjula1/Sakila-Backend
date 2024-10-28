package com.CS490.sakila.controller;

import com.CS490.sakila.dto.RentalDTO;
import com.CS490.sakila.model.Customer;
import com.CS490.sakila.model.Inventory;
import com.CS490.sakila.model.Rental;
import com.CS490.sakila.model.Staff;
import com.CS490.sakila.repository.CustomerRepository;
import com.CS490.sakila.repository.InventoryRepository;
import com.CS490.sakila.repository.RentalRepository;
import com.CS490.sakila.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    // Endpoint to rent a film to a customer
    @PostMapping("/rent")
    public RentalDTO rentFilm(@RequestBody RentalDTO rentalDTO) {
        System.out.println("Received Rental Request: " + rentalDTO);

        // Validate RentalDTO fields
        if (rentalDTO.getFilmId() == null || rentalDTO.getFilmId() <= 0) {
            throw new IllegalArgumentException("Invalid Film ID.");
        }
        if (rentalDTO.getCustomerId() == null || rentalDTO.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Invalid Customer ID.");
        }
        if (rentalDTO.getStaffId() == null || rentalDTO.getStaffId() <= 0) {
            throw new IllegalArgumentException("Invalid Staff ID.");
        }

        // Fetch Customer and Staff
        Optional<Customer> customerOptional = customerRepository.findById(rentalDTO.getCustomerId());
        Optional<Staff> staffOptional = staffRepository.findById(rentalDTO.getStaffId());

        if (customerOptional.isEmpty()) {
            throw new IllegalArgumentException("Customer not found for ID: " + rentalDTO.getCustomerId());
        }
        if (staffOptional.isEmpty()) {
            throw new IllegalArgumentException("Staff not found for ID: " + rentalDTO.getStaffId());
        }

        Customer customer = customerOptional.get();
        Staff staff = staffOptional.get();

        // Fetch all inventories for the selected film
        List<Inventory> inventories = inventoryRepository.findByFilmFilmId(rentalDTO.getFilmId());

        if (inventories.isEmpty()) {
            throw new IllegalArgumentException("No inventories found for Film ID: " + rentalDTO.getFilmId());
        }

        // Find the first available inventory (no active rentals)
        Optional<Inventory> availableInventoryOpt = inventories.stream()
                .filter(inv -> rentalRepository.findActiveRentalsByFilmId(inv.getInventoryId()).isEmpty())
                .findFirst();

        if (availableInventoryOpt.isEmpty()) {
            throw new IllegalArgumentException("No available inventories for Film ID: " + rentalDTO.getFilmId());
        }

        Inventory availableInventory = availableInventoryOpt.get();

        // Create and save the rental
        Rental rental = new Rental();
        rental.setInventory(availableInventory);
        rental.setCustomer(customer);
        rental.setStaff(staff);
        rental.setRentalDate(LocalDateTime.now());

        rental = rentalRepository.save(rental);

        // Return the created rental details
        return new RentalDTO(
                rental.getRentalId(),
                rental.getInventory().getInventoryId(),
                rental.getCustomer().getCustomerId(),
                rental.getStaff().getStaffId(),
                rental.getRentalDate(),
                rental.getReturnDate()
        );
    }

    // Endpoint to mark a rental as returned
    @PutMapping("/return/{rentalId}")
    public String returnRental(@PathVariable int rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + rentalId));

        if (rental.getReturnDate() != null) {
            return "Rental ID " + rentalId + " has already been returned.";
        }

        rental.setReturnDate(LocalDateTime.now());
        rentalRepository.save(rental);

        return "Rental ID " + rentalId + " has been marked as returned.";
    }
}


