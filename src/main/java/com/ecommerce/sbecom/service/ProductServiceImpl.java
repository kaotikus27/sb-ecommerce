package com.ecommerce.sbecom.service;


import com.ecommerce.sbecom.exceptions.ResourceNotFound;
import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import com.ecommerce.sbecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryReposiory;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {





        Category category = categoryReposiory.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFound(
                        "Category",
                        "categoryId",
                        categoryId));
        product.setCategory(category);

        //price * quamtity
        double specialPrice = product.getPrice();

        return null;
    }
}
