package com.ecommerce.shopping_cart.service;

import com.ecommerce.shopping_cart.entity.Category;
import com.ecommerce.shopping_cart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository repo;

    @Override
    public Category saveCategory(Category category) {
        return repo.save(category);
    }

    @Override
    public Boolean existCategory(String name) {
        return repo.existsByName(name);
    }

    @Override
    public List<Category> getAllCategory() {
        return repo.findAll();
    }

    @Override
    public Boolean deleteCategory(int id) {
       Category category =repo.findById(id).orElse(null);
       if(!ObjectUtils.isEmpty(category)){
           repo.delete(category);
           return true;
       }
       return false;

    }

    @Override
    public Category getCategoryById(int id) {
        Category category=repo.findById(id).orElse(null);
        return category;
    }

    @Override
    public List<Category> getAllActiveCategory() {
        return repo.findByIsActiveTrue();
    }
}
