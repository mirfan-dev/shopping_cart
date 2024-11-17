package com.ecommerce.shopping_cart.repository;

import com.ecommerce.shopping_cart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    public User findByEmail(String email);
}
