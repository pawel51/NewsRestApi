package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Category;

import java.util.List;

public interface CategoryService {
    Category create(Category category);
    void delete(Category category);
    Category update(Category category);
    Category findById(Long id);
    List<Category> findAll();
    boolean IsCategoryNameInDatabase(String name);
}
