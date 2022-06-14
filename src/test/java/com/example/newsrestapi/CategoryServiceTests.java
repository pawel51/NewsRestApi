package com.example.newsrestapi;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.repository.CategoryRepository;
import com.example.newsrestapi.service.AnnouncementService;
import com.example.newsrestapi.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CategoryServiceTests {

    @Autowired
    CategoryService categoryService;
    @Autowired
    AnnouncementService announcementService;
    @Test
    public void create_ShouldCreateCategory()
    {
        //arrange
        Category category = new Category();
        category.setName("JobsOffers");

        //act
        categoryService.create(category);

        //assert
        Assertions.assertNotNull(category.getId());
    }
    @Test
    public void findAll_ShouldFindTwoCategories()
    {
        //arrange
        Category category = new Category();
        category.setName("JobsOffers");
        Category category2 = new Category();
        category2.setName("JobsOffers2");
        categoryService.create(category);
        categoryService.create(category2);

        //act
        List<Category> categories = categoryService.findAll();

        //assert
        Assertions.assertEquals(2, categories.size());
    }
    @Test
    public void findById()
    {
        //arrange
        Category category = CreateCategory();

        //act
        Category categoryFromDB = categoryService.findById(category.getId());

        //assert
        Assertions.assertEquals(category.getId(), categoryFromDB.getId());
    }
    @Test
    public void delete_ShouldDeleteOneCategory()
    {
        //arrange
        Category category = CreateCategory();
        Long id = category.getId();

        //act
        categoryService.delete(category);
        Category category2 = categoryService.findById(id);

        //assert
        Assertions.assertNull(category2);
    }
    @Test
    public void update_ShouldUpdateName()
    {
        //arrange
        Category category = CreateCategory();
        String newName = "UpdateJobOffers";

        //act
        category.setName(newName);
        categoryService.update(category);
        Category categoryFromDB = categoryService.findById(category.getId());

        //assert
        Assertions.assertEquals(categoryFromDB.getName(), category.getName());
    }
    @Test
    public void deleteAll_ShouldDeleteAllCategories()
    {
        //arrange
        Category category = CreateCategory();
        Category category2 = CreateCategory("NewJobOffers");

        //act
        categoryService.deleteAll();
        List<Category> categories = categoryService.findAll();

        //assert
        Assertions.assertEquals(categories.size(), 0);
    }
    @Test
    public void IsCategoryNameInDatabase()
    {
        //arrange
        Category category = CreateCategory();

        //act
        boolean IsCategoryNameInDatabase = categoryService.IsCategoryNameInDatabase(category.getName());

        //assert
        Assertions.assertTrue(IsCategoryNameInDatabase);
    }
    @BeforeEach
    public void Clean()
    {
        announcementService.deleteAll();
        categoryService.deleteAll();
    }
    private Category CreateCategory()
    {
        return CreateCategory("JobOffers");
    }
    private Category CreateCategory(String name)
    {
        Category category = new Category();
        category.setName(name);
        categoryService.create(category);
        return category;
    }
}
