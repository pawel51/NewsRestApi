package com.example.newsrestapi.api;

import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category)
    {
        if(categoryService.IsCategoryNameInDatabase(category.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is already in a category table");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.create(category));
        }
    }
    @GetMapping
    public ResponseEntity<List<Category>> getCategories()
    {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll());
    }
    @GetMapping(path = "{id}")
    public ResponseEntity<Category> getCategoryByID(@PathVariable("id") Long id)
    {
        Category category = categoryService.findById(id);
        if(category == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(category);
        }
    }
    @DeleteMapping(path = "{id}")
    public void deleteCategory(@PathVariable("id") Long id)
    {
        Category category = categoryService.findById(id);
        if(category == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else
        {
            categoryService.delete(category);
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id, @RequestBody Category category)
    {
        Category categoryFromDB = categoryService.findById(id);
        if(categoryFromDB == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else
        {
            category.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(category));
        }
    }
}
