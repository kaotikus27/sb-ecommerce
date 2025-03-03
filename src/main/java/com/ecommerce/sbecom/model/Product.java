package com.ecommerce.sbecom.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@ToString
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;


    @NotBlank
    @Size(min = 5, message = "productName  must contain atleast 5 characters")
    private String productName;


    @NotBlank
    @Size(min = 5, message = "description  must contain atleast 5 characters")
    private String description;


    private String image;


    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;


    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "1.0", message = "Quantity must be at least 1.0")
    private double price;

    private double discount;

    private double specialPrice;

    @ManyToOne
    @JoinColumn(name= "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

}
