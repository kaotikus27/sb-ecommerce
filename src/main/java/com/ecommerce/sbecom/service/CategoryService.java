package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.payload.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CategoryService  {

    CategoryResponse getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);


    Category updateCategory(Category category, Long categoryId);

}
