package com.ecommerce.shopping_cart.service;

import com.ecommerce.shopping_cart.entity.Product;
import com.ecommerce.shopping_cart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository repo;

    @Override
    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Boolean deleteProducts(Integer id) {
       Product product = repo.findById(id).orElse(null);

       if(!ObjectUtils.isEmpty(product)){
           repo.deleteById(id);
           return true;
       }
       return false;
    }

    @Override
    public Product getProductById(Integer id) {
        Product product=repo.findById(id).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image)  {
           Product dbProduct=getProductById(product.getId());
           String imageName=image.isEmpty()? dbProduct.getImage():image.getOriginalFilename();
           dbProduct.setTitle(product.getTitle());
           dbProduct.setImage(imageName);
           dbProduct.setCategory(product.getCategory());
           dbProduct.setPrice(product.getPrice());
           dbProduct.setDescription(product.getDescription());
           dbProduct.setStock(product.getStock());
           dbProduct.setDiscount(product.getDiscount());
           dbProduct.setIsActive(product.getIsActive());

              // 100*(5/100)=5   100-5=95
           Double discount=product.getPrice()*(product.getDiscount()/100.0);
           Double discountPrice=product.getPrice()-discount;
           dbProduct.setDiscountPrice(discountPrice);

           Product updatePrdouct=repo.save(dbProduct);



           if(!ObjectUtils.isEmpty(updatePrdouct)){
                if(!image.isEmpty()){
                    try {

                        File saveFile = new ClassPathResource("static/image").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());
                        //System.out.println(path);
                        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
           }
                return product;
       }
        return null;
    }

    @Override
    public List<Product> getAllIsActiveProducts(String category) {

        List<Product> products=null;
        if(ObjectUtils.isEmpty(category)){
            products=repo.findByIsActiveTrue();
        }else {
            products=repo.findByCategory(category);
        }
        return products;
    }


}
