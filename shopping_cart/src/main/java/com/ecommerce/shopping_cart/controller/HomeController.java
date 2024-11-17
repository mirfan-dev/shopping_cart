package com.ecommerce.shopping_cart.controller;

import com.ecommerce.shopping_cart.entity.Category;
import com.ecommerce.shopping_cart.entity.Product;
import com.ecommerce.shopping_cart.entity.User;
import com.ecommerce.shopping_cart.service.CategoryService;
import com.ecommerce.shopping_cart.service.ProductService;
import com.ecommerce.shopping_cart.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService catService;

    @Autowired
    private ProductService proService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){

        if(p!=null){
            String email=p.getName();
            User userDtls=userService.getUserByEmail(email);
            m.addAttribute("user",userDtls);
        }
        List<Category> allActiveCategory=catService.getAllActiveCategory();
        m.addAttribute("categories",allActiveCategory);

    }

    @GetMapping("/")
    public String index(){
        return "Index";
    }

    @GetMapping("/signin")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @GetMapping("/products")
    public String products(Model m, @RequestParam(value = "category", defaultValue = "") String category){
        List<Category> categories=catService.getAllActiveCategory();
        List<Product> products=proService.getAllIsActiveProducts(category);
        m.addAttribute("categories", categories);
        m.addAttribute("products",products);
        m.addAttribute("paramValue" , category);
        return "product";
    }

    @GetMapping("/product_view/{id}")
    public String product(@PathVariable int id, Model m){
        Product productById=proService.getProductById(id);
        m.addAttribute("p",productById);
        return "view_product";
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, @RequestParam("img") MultipartFile file){
        String imageName=file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        User saveUser=userService.saveUser(user);
        if(!ObjectUtils.isEmpty(saveUser)){
            if(!file.isEmpty()) {
                try {
                    File saveFile = new ClassPathResource("static/image").getFile();
                    Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+ "profile_img" +File.separator
                         +file.getOriginalFilename());
                    Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                }catch(Exception e){
                    e.printStackTrace();

                }
                session.setAttribute("succMsg","Registered successfully");

            }else{
                session.setAttribute("errorMsg","Something wrong on server");
            }
        }
        return "redirect:/register";
    }




}
