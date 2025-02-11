package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.exceptions.APIException;
import com.ecommerce.sbecom.exceptions.ResourceNotFound;
import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.payload.CategoryResponse;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryResponse getAllCategories() {
        List < Category > categories = categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No category created till now");
        return null;
    }

    @Override
    public void createCategory(Category category) {

        Category savedCategory =
                categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)
            throw new APIException
                    ("Category with the name "
                     + category.getCategoryName()
                     + "Already exists ");
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        //List<Category> categories = categoryRepository.findAll();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFound
                        ("Category", "CategoryId", categoryId));
//        Category category = categories.stream()
//                .filter(c-> c.getCategoryId().equals(categoryId))
//                .findFirst()
//                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
//        if(category == null) {
//                return "Category not found";
//        }
        categoryRepository.delete(category);
        return "successfully deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        //Optional<Category> saveCategoryOptional = categoryRepository.findById(categoryId);

        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setCategoryId(categoryId);
        return categoryRepository.save(category);


//        Optional<Category> optionalCategory  = categories.stream()
//                .filter(c-> c.getCategoryId().equals(categoryId))
//                .findFirst();
//
//        if(optionalCategory.isPresent()) {
//            Category exsistingCategory = optionalCategory.get();
//            exsistingCategory.setCategoryName(category.getCategoryName());
//            Category savedCategory = categoryRepository.save(exsistingCategory);
//            return savedCategory;
//        } else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
//        }

    }
}
