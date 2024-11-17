package com.ecommerce.shopping_cart.service;

import com.ecommerce.shopping_cart.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProducts(Integer id);

    public Product getProductById(Integer id);

    public Product updateProduct(Product product, MultipartFile image) ;

    public List<Product> getAllIsActiveProducts(String category);


}
