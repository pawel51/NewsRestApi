package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Category;

import java.util.List;

public interface CategoryService {
    void create(Category category);
    void delete(Category category);
    void update(Category category);
    Category findById(Long id);
    List<Category> findAll();
}
