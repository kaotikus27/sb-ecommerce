package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.payload.CategoryDTO;
import com.ecommerce.sbecom.payload.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CategoryService  {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);

    CategoryDTO createCategory(CategoryDTO categoryDTO);


    CategoryDTO deleteCategory( Long categoryId);


    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

}
