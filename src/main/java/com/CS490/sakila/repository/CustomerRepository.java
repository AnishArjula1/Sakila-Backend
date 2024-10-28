package com.CS490.sakila.repository;

import com.CS490.sakila.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findAll(Pageable pageable);

    Page<Customer> findByCustomerId(int customerId, Pageable pageable);

    Page<Customer> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<Customer> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}
