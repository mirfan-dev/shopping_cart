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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService service;

    @Autowired
    private ProductService proService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {

        if (p != null) {
            String email = p.getName();
            User userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
        }
        List<Category> allActiveCategory=service.getAllActiveCategory();
        m.addAttribute("categories",allActiveCategory);
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m){
        List<Category> categories=service.getAllCategory();
        m.addAttribute("categories",categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model m){
        m.addAttribute("categorys",service.getAllCategory());
        return "admin/category";
    }

//   Start Category Class implementation

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file) throws IOException {

        String imageName=file!=null?file.getOriginalFilename():"default.jpg";
        category.setImageName(imageName);
        Boolean exist=service.existCategory(category.getName());
        if(exist){
            session.setAttribute("errorMsg","Category Name already exist");

        }else{
            Category save=service.saveCategory(category);
            if(ObjectUtils.isEmpty(save)){
                session.setAttribute("errorMsg","Internal server error");
            }else{
                File saveFile=new ClassPathResource("static/image").getFile();
                Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());
                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg","saved Successfully");
            }
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id){
        Boolean category=service.deleteCategory(id);
        if(category){
            session.setAttribute("succMsg", "Category deleted successfully");
        }else{
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id,Model m){
        m.addAttribute("category",service.getCategoryById(id));
        return "admin/edit_category";

    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file){
        Category oldCategory=service.getCategoryById(category.getId());
        String imageName=file.isEmpty()? oldCategory.getImageName(): file.getOriginalFilename();
        if(!ObjectUtils.isEmpty(oldCategory)){
            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }
        Category updateCategory=service.saveCategory(oldCategory);
        if(!ObjectUtils.isEmpty(updateCategory)){
            session.setAttribute("succMsg","Category update successuflly");
        }else{
            session.setAttribute("errorMsg", "Category id not found");
        }
        return  "redirect:/admin/loadEditCategory/" + category.getId();
    }

    // End Categroy class implementation


    // Start Product class implementation

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image) throws IOException {

        String imageName=image.isEmpty() ? "default.jpg" :image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        Product product1=proService.saveProduct(product);
        if(!ObjectUtils.isEmpty(product1)){
            File saveFile=new ClassPathResource("static/image").getFile();
            Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+image.getOriginalFilename());
            System.out.println(path);
            Files.copy(image.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            session.setAttribute("succMsg", "Product saved successfully");
        }else{
            session.setAttribute("errorMsg", "Product not saved");
        }
        return "redirect:/admin/loadAddProduct";

    }

    @GetMapping("/products")
    public String loadViewProduct(Model m){
        m.addAttribute("products",proService.getAllProducts());
        return "admin/product";
    }

    @GetMapping("/deleteProducts/{id}")
    public String deleteProduct(@PathVariable Integer id){
        Boolean deleteProduct=proService.deleteProducts(id);
        if(deleteProduct){
            session.setAttribute("succMsg","Product Deleted Successfully");
        }else{
            session.setAttribute("errorMsg","Id not found");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Integer id, Model m){
        m.addAttribute("product",proService.getProductById(id));
        m.addAttribute("categories",service.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String editProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image, Model m){


        if(product.getDiscount() < 0 || product.getDiscount() > 100){
            session.setAttribute("errorMsg", "Invalid Discount");
        }else {
            Product updateProduct=proService.updateProduct(product,image);

            if(!ObjectUtils.isEmpty(updateProduct)){
                session.setAttribute("succMsg","Product updated successfully");
            }else{
                session.setAttribute("errorMsg","something wrong on server");
            }
        }

        return "redirect:/admin/editProduct/" + product.getId();
    }



    // Start Product class implementation


}
