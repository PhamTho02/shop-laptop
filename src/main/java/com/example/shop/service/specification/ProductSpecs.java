package com.example.shop.service.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.shop.domain.Product;
import com.example.shop.domain.Product_;

public class ProductSpecs {
    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    }
}
