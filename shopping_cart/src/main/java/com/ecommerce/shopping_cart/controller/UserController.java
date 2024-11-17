package com.ecommerce.shopping_cart.controller;

import com.ecommerce.shopping_cart.entity.Category;
import com.ecommerce.shopping_cart.entity.User;
import com.ecommerce.shopping_cart.service.CategoryService;
import com.ecommerce.shopping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService catService;

    @GetMapping("/")
    public String home(){

        return "user/home";

    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {

        if (p != null) {
            String email = p.getName();
            User userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
        }
        List<Category> allActiveCategory=catService.getAllActiveCategory();
        m.addAttribute("categories",allActiveCategory);
    }
}
