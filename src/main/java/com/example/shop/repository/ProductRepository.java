package com.example.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods can be defined here if needed

}
