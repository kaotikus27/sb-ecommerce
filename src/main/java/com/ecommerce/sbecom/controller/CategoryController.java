package com.ecommerce.sbecom.controller;


import com.ecommerce.sbecom.model.Category;

import com.ecommerce.sbecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/api")
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(
            CategoryService categoryService )
        {
            this.categoryService = categoryService;
        }

//  http://localhost:8080/api/public/categories
//  @GetMapping("api/public/categories")
    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity <List<Category>> getAllCategories(){
        List<Category> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK) ;
    }
    //    public List<Category> getAllCategories(){
    //        return categoryService.getAllCategories();
    //    }


//  @PostMapping("api/public/categories")
    @RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>("category added successfully", HttpStatus.CREATED);
    }

    //http://localhost:8080/api/admin/categories/2
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){

        try {
            String status = categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(status);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
            }
        }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category,
                                                 @PathVariable Long categoryId){
    try{
        Category savedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>("category with category id"+ categoryId, HttpStatus.OK);
    }catch (ResponseStatusException e){
        return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
