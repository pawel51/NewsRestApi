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
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public Category update(Category category) {
        return categoryRepository.save(category);
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
        return categoryRepository.GetCategories();
    }
    @Override
    public boolean IsCategoryNameInDatabase(String name)
    {
        return categoryRepository.IsCategoryNameInDatabase(name);
    }
}
