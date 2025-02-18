package com.ecommerce.sbecom.controller;


import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class ProductController {


    @Autowired
    ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody  Product product,
                                                 @PathVariable Long categoryId){



       ProductDTO productDTO =  productService.addProduct(categoryId, product);

       return new ResponseEntity<>(productDTO, HttpStatus.CREATED);

    }

}
