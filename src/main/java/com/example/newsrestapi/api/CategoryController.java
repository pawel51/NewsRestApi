package com.example.newsrestapi.api;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.service.CategoryService;
import com.example.newsrestapi.service.EmailService;
import com.example.newsrestapi.service.EmailServiceImpl;
import com.example.newsrestapi.utils.enums.RolesEnum;
import dto.AnnouncementDTO;
import dto.CategoryDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO)
    {
        UserSecurityCheck();
        if(categoryService.IsCategoryNameInDatabase(categoryDTO.getName()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is already in a category table");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.create(ConvertFromDTO(List.of(categoryDTO)).get(0)));
        }
    }
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories()
    {
        return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(categoryService.findAll()));
    }
    @GetMapping(path = "{id}")
    public ResponseEntity<CategoryDTO> getCategoryByID(@PathVariable("id") Long id)
    {
        Category category = categoryService.findById(id);
        if(category == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(List.of(category)).get(0));
        }
    }
    @DeleteMapping(path = "{id}")
    public void deleteCategory(@PathVariable("id") Long id)
    {
        UserSecurityCheck();
        Category category = categoryService.findById(id);
        if(category == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else
        {
            try
            {
                categoryService.delete(category);
            } catch (Exception e)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO)
    {
        UserSecurityCheck();
        Category categoryFromDB = categoryService.findById(id);
        if(categoryFromDB == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else
        {
            categoryDTO.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(ConvertFromDTO(List.of(categoryDTO)).get(0)));
        }
    }
    private void UserSecurityCheck()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            throw new ResponseStatusException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "You must be logged in");
        }
        else if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolesEnum.ROLE_ADMIN.toString()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must be admin");
        }
    }
    private List<CategoryDTO> ConvertToDTO(List<Category> categoryList)
    {
        List<CategoryDTO> categoryDTOs = new ArrayList<CategoryDTO>();
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            categoryDTOs.add(categoryDTO);
        }
        return  categoryDTOs;
    }
    private List<Category> ConvertFromDTO(List<CategoryDTO> categoryList)
    {
        List<Category> convertedFromDTOAnnouncements = new ArrayList<Category>();
        for (CategoryDTO categoryDTO : categoryList) {
            Category category = modelMapper.map(categoryDTO, Category.class);
            convertedFromDTOAnnouncements.add(category);
        }
        return  convertedFromDTOAnnouncements;
    }
}
