package com.ecommerce.sbecom.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "street name must be atleast 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "buildingName name must be atleast 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "city name must be atleast 5 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "state name must be atleast 2 characters")
    private String state;

    @NotBlank
    @Size(min = 6, message = "pincode name must be atleast 6 characters")
    private String pincode;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

}
