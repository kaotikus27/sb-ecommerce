package com.ecommerce.sbecom.service;


import com.ecommerce.sbecom.exceptions.APIException;
import com.ecommerce.sbecom.exceptions.ResourceNotFound;
import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.payload.ProductResponse;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import com.ecommerce.sbecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @Value("${project.image}")
    private String path;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryReposiory.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFound(
                        "Category",
                        "categoryId",
                        categoryId));

        //check if empty or not
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        //for (int i = 0; i < products.size(); i++)
        for (Product value: products) {
            //if(products.get(i).getProductName().equals(productDTO.getProductName()))
            if(value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent) {
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
        } else {
            throw new APIException("product already exists");
        }


    }

    @Override
    public ProductResponse getAllProducts(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProduct = productRepository.findAll(pageDetails);

        List<Product> products = pageProduct.getContent();

       List<ProductDTO> productDTOS = products.stream()
               .map(product -> modelMapper.map(product, ProductDTO.class))
               .toList();

       ProductResponse productResponse = new ProductResponse();
       productResponse.setContent(productDTOS);

       productResponse.setPageNumber(pageProduct.getNumber());
       productResponse.setPageSize(pageProduct.getSize());
       productResponse.setTotalElements(pageProduct.getTotalElements());
       productResponse.setTotalPages(pageProduct.getTotalPages());
       productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(
            Long categoryId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Category category = categoryReposiory.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFound(
                        "Category",
                        "categoryId",
                        categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProduct = productRepository.findByCategoryOrderByPriceAsc
                (category, pageDetails);

        List<Product> products = pageProduct.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();


        if(products.size() == 0) {
            throw new APIException(category.getCategoryName() +" Category does not exist ");
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElements(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(
            String keyword,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
            ) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProduct = productRepository.findByProductNameLikeIgnoreCase
                ('%' + keyword + '%', pageDetails);

        List<Product> products = pageProduct.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        if(products.size() == 0) {
            throw new APIException("product not found with keyword " + keyword);
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElements(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());

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

        String fileName = fileService.uploadImage(path, image);

        // Updating the new file name to the product
        productFromDb.setImage(fileName);

        //save updated product
        Product updateProduct = productRepository.save(productFromDb);

        // return DTO after mapping product to DTO
        return modelMapper.map(updateProduct, ProductDTO.class);
    }




}
