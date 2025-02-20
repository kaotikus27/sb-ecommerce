package com.ecommerce.sbecom.controller;


import com.ecommerce.sbecom.config.AppConstants;
import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.CategoryDTO;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.payload.ProductResponse;
import com.ecommerce.sbecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class ProductController {



    @Autowired
    ProductService productService;



    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid
                                                 @RequestBody  ProductDTO productDTO,
                                                 @PathVariable Long categoryId){

       ProductDTO savedProductDTO =  productService.addProduct(categoryId, productDTO);

       return new ResponseEntity<>(productDTO, HttpStatus.CREATED);

    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",
                    defaultValue =AppConstants.PAGE_SIZE,
                    required = false) Integer pageSize,
            @RequestParam(name = "sortBy" ,
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false) String sortBy,
            @RequestParam(name = "sortOrder",
                    defaultValue = AppConstants.SORT_CATEGORIES_DIR,
                    required = false) String sortOrder
            ) {
        ProductResponse productResponse = productService.getAllProducts(
                pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }


    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(
            @PathVariable Long categoryId ,
            @RequestParam(name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",
                    defaultValue =AppConstants.PAGE_SIZE,
                    required = false) Integer pageSize,
            @RequestParam(name = "sortBy" ,
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false) String sortBy,
            @RequestParam(name = "sortOrder",
                    defaultValue = AppConstants.SORT_CATEGORIES_DIR,
                    required = false) String sortOrder){

        ProductResponse productResponse = productService.searchByCategory
                (categoryId, pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }


    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @PathVariable String keyword){


        ProductResponse productResponse=  productService.searchByKeyword(
                keyword );

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }



    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct
            (@RequestBody ProductDTO productDTO,
             @PathVariable Long productId){

       ProductDTO updatedProductDTO =  productService.updateProduct(productId, productDTO);

       return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);

    }


    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(

            @PathVariable Long productId){
        ProductDTO status = productService.deleteCategory(productId);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage
            (@PathVariable Long productId,
             @RequestParam("image")MultipartFile image) throws IOException {

       ProductDTO updateProduct =  productService.updateProductImage(productId, image);

        return new ResponseEntity<>(updateProduct, HttpStatus.OK);

    }
}
