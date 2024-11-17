package com.ecommerce.shopping_cart.config;

import com.ecommerce.shopping_cart.entity.User;
import com.ecommerce.shopping_cart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=repo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("user not found");
        }else {
            return new CustomUser(user);
        }
    }
}
