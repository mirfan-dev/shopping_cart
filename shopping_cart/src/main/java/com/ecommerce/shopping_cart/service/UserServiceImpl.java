package com.ecommerce.shopping_cart.service;

import com.ecommerce.shopping_cart.entity.User;
import com.ecommerce.shopping_cart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return repo.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return repo.findByEmail(email);
    }
}
