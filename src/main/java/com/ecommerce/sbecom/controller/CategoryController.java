package com.ecommerce.sbecom.controller;


import com.ecommerce.sbecom.config.AppConstants;
import com.ecommerce.sbecom.model.Category;

import com.ecommerce.sbecom.payload.CategoryDTO;
import com.ecommerce.sbecom.payload.CategoryResponse;
import com.ecommerce.sbecom.service.CategoryService;
import jakarta.validation.Valid;
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


    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(
            @RequestParam(name = "message", required = false) String message
    )
    {
        return new ResponseEntity<>("Echoed message:"+ message,  HttpStatus.OK);
    }

    @Autowired
    public CategoryController(
            CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",
                    defaultValue =AppConstants.PAGE_NUMBER,
                    required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",
                    defaultValue =AppConstants.PAGE_SIZE,
                    required = false) Integer pageSize,
            @RequestParam(name = "sortBy" ,
                    defaultValue = AppConstants.SORT_CATEGORIES_BY,
                    required = false) String sortBy,
            @RequestParam(name = "sortOrder",
                    defaultValue = AppConstants.SORT_CATEGORIES_DIR,
                    required = false) String sortOrder
    )
    {
        CategoryResponse categoryResponse =
                categoryService.getAllCategories(pageNumber, pageSize ,sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }






    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO =  categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>( savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(
            @PathVariable Long categoryId) {
        CategoryDTO status = categoryService.deleteCategory( categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @PathVariable Long categoryId) {

        CategoryDTO savedCategoryDTO =
                categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);

    }
}