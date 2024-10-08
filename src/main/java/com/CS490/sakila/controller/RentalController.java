package com.CS490.sakila.controller;

import com.CS490.sakila.dto.RentalDTO;
import com.CS490.sakila.model.Rental;
import com.CS490.sakila.model.Inventory;
import com.CS490.sakila.model.Customer;
import com.CS490.sakila.model.Staff;
import com.CS490.sakila.repository.RentalRepository;
import com.CS490.sakila.repository.InventoryRepository;
import com.CS490.sakila.repository.CustomerRepository;
import com.CS490.sakila.repository.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(rentalDTO.getInventoryId());
        Optional<Customer> customerOptional = customerRepository.findById(rentalDTO.getCustomerId());
        Optional<Staff> staffOptional = staffRepository.findById(rentalDTO.getStaffId());

        // Validate inventory, customer, and staff
        if (inventoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Inventory not found for ID: " + rentalDTO.getInventoryId());
        }
        if (customerOptional.isEmpty()) {
            throw new IllegalArgumentException("Customer not found for ID: " + rentalDTO.getCustomerId());
        }
        if (staffOptional.isEmpty()) {
            throw new IllegalArgumentException("Staff not found for ID: " + rentalDTO.getStaffId());
        }

        Inventory inventory = inventoryOptional.get();
        Customer customer = customerOptional.get();
        Staff staff = staffOptional.get();

        // Create and save the rental
        Rental rental = new Rental();
        rental.setInventory(inventory);
        rental.setCustomer(customer);
        rental.setStaff(staff);
        rental.setRentalDate(LocalDateTime.now());

        rental = rentalRepository.save(rental);

        // Return the created rental details
        return new RentalDTO(
                rental.getRentalId(), 
                rental.getInventory().getFilm().getFilmId(),
                rental.getCustomer().getCustomerId(), 
                rental.getStaff().getStaffId(), 
                rental.getRentalDate()
        );
    }
}


