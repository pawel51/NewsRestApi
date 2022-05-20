package com.example.newsrestapi.repository;

import com.example.newsrestapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> GetCategories();
    @Query("SELECT count(c)>0 FROM Category c where c.name = :name")
    boolean IsCategoryNameInDatabase(String name);
}
