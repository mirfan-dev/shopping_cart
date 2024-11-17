package com.ecommerce.shopping_cart.repository;

import com.ecommerce.shopping_cart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {


    public List<Product> findByIsActiveTrue();

    public List<Product> findByCategory(String category);
}
