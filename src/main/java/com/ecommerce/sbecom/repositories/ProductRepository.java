package com.ecommerce.sbecom.repositories;

import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Page <Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    Product findByProductName( String productName);
}
