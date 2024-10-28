package com.CS490.sakila.repository;

import com.CS490.sakila.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}
