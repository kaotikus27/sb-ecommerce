package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories = new ArrayList<>();

    private Long nextId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category category = categories.stream()
                .filter(c-> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        if(category == null) {
                return "Category not found";
        }
        categories.remove(category);
        return "successfully deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Optional<Category> optionalCategory  = categories.stream()
                .filter(c-> c.getCategoryId().equals(categoryId))
                .findFirst();

        if(optionalCategory.isPresent()) {
            Category exsistingCategory = optionalCategory.get();
            exsistingCategory.setCategoryName(category.getCategoryName());
            return exsistingCategory;
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

    }
}
