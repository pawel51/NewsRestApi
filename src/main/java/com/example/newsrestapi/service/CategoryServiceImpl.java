package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void create(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public void update(Category category) {
        Category categoryFromDB = categoryRepository.findById(category.getId()).get();
        categoryFromDB.setName(category.getName());
        categoryRepository.save(categoryFromDB);
    }

    @Override
    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent())
        {
            return category.get();
        }
        else{
            return null;
        }
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
