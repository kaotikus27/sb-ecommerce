package com.ecommerce.sbecom.service;


import com.ecommerce.sbecom.exceptions.ResourceNotFound;
import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.payload.ProductResponse;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import com.ecommerce.sbecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryReposiory;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private FileService fileService;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {


        Category category = categoryReposiory.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFound(
                        "Category",
                        "categoryId",
                        categoryId));

        Product product = modelMapper.map(productDTO, Product.class);

        product.setImage("default image");
        product.setCategory(category);

        //Special price formula =  price - ((discount/100) * price)
        double specialPrice =
                product.getPrice() -
                        ((product.getDiscount() * 0.01) * product.getPrice());

        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {

       List<Product> products =  productRepository.findAll();
       List<ProductDTO> productDTOS = products.stream()
               .map(product -> modelMapper.map(product, ProductDTO.class))
               .toList();

       ProductResponse productResponse = new ProductResponse();
       productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {

        Category category = categoryReposiory.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFound(
                        "Category",
                        "categoryId",
                        categoryId));

        List<Product> products =
                productRepository.findByCategoryOrderByPriceAsc(category);

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword) {

        List<Product> products =
                productRepository.
                        findByProductNameLikeIgnoreCase
                                ('%' + keyword + '%');

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        //get existing product from DB

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFound(
                        "Product",
                        "productId",
                        productId));

        Product product = modelMapper.map(productDTO, Product.class);

        //update product info with the one in request body
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        //save to DB
        Product savedProduct = productRepository.save(productFromDb);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteCategory(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->
                        new ResourceNotFound
                                (
                                        "Product",
                                        "productId",
                                        productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);

    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        // Get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFound(
                        "Product",
                        "productId",
                        productId));
        // Upload the image to server
        // Get the file name of uploaded image
        String path = "images/";
        String fileName = fileService.uploadImage(path, image);

        // Updating the new file name to the product
        productFromDb.setImage(fileName);

        //save updated product
        Product updateProduct = productRepository.save(productFromDb);

        // return DTO after mapping product to DTO
        return modelMapper.map(updateProduct, ProductDTO.class);
    }




}
