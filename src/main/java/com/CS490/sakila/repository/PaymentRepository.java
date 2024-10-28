package com.CS490.sakila.repository;

import com.CS490.sakila.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    @Transactional
    void deleteByCustomerCustomerId(int customerId);  // Delete payments by customer ID
}
