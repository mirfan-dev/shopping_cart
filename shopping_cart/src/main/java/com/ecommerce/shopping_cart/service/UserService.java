package com.ecommerce.shopping_cart.service;

import com.ecommerce.shopping_cart.entity.User;

public interface UserService {

    public User saveUser(User user);

    public User getUserByEmail(String email);
}
