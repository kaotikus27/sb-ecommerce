package com.ecommerce.sbecom.controller;


import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.CategoryDTO;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.payload.ProductResponse;
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


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse productResponse = productService.getAllProducts();

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }


    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory( @PathVariable Long categoryId){

        ProductResponse productResponse = productService.searchByCategory(categoryId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }


    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword){


        ProductResponse productResponse=  productService.searchByKeyword(keyword);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }



    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct
            (@RequestBody Product product,
             @PathVariable Long productId){

       ProductDTO updatedProduct =  productService.updateProduct(productId, product);

       return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }


    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(

            @PathVariable Long productId){
        ProductDTO status = productService.deleteCategory(productId);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }
}
