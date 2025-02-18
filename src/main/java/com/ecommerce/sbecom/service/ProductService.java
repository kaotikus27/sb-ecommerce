package com.ecommerce.sbecom.service;


import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {


    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO product);

    ProductDTO deleteCategory(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
