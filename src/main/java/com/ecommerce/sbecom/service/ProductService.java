package com.ecommerce.sbecom.service;


import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.ProductDTO;

public interface ProductService {


    ProductDTO addProduct(Long categoryId, Product product);
}
