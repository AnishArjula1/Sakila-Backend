package com.CS490.sakila.repository;

import com.CS490.sakila.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}
