package com.CS490.sakila.dto;

import java.time.LocalDateTime;

public class RentalDTO {
    private Integer rentalId;
    private Integer filmId; // Added filmId
    private Integer customerId;
    private Integer staffId;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;

    // Constructors
    public RentalDTO() {}

    public RentalDTO(Integer rentalId, Integer filmId, Integer customerId, Integer staffId, LocalDateTime rentalDate, LocalDateTime returnDate) {
        this.rentalId = rentalId;
        this.filmId = filmId;
        this.customerId = customerId;
        this.staffId = staffId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public Integer getFilmId() { // Added getter
        return filmId;
    }

    public void setFilmId(Integer filmId) { // Added setter
        this.filmId = filmId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "RentalDTO{" +
                "rentalId=" + rentalId +
                ", filmId=" + filmId +
                ", customerId=" + customerId +
                ", staffId=" + staffId +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                '}';
    }
}

