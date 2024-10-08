package com.CS490.sakila.dto;

import java.time.LocalDateTime;

public class RentalDTO {
    private int rentalId;
    private int inventoryId;  // Add inventory ID to link the rental
    private int customerId;
    private int staffId;
    private LocalDateTime rentalDate;

    // Constructors
    public RentalDTO() {}

    public RentalDTO(int rentalId, int inventoryId, int customerId, int staffId, LocalDateTime rentalDate) {
        this.rentalId = rentalId;
        this.inventoryId = inventoryId;
        this.customerId = customerId;
        this.staffId = staffId;
        this.rentalDate = rentalDate;
    }

    // Getters and Setters

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }
}

