package com.ecommerce.sbecom.controller;


import com.ecommerce.sbecom.model.Category;

import com.ecommerce.sbecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@RestController
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(
            CategoryService categoryService )
        {
            this.categoryService = categoryService;
        }

        //http://localhost:8080/api/public/categories
    @GetMapping("api/public/categories")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }


    @PostMapping("api/public/categories")
    public String createCategory(@RequestBody Category category){

        categoryService.createCategory(category);

        return "category added successfully";
    }

    //http://localhost:8080/api/admin/categories/2
    @DeleteMapping("/api/admin/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){

        String status = categoryService.deleteCategory(categoryId);

        return status;
    }

}
