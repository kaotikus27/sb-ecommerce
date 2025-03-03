package com.ecommerce.sbecom.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer roleId;


    @Enumerated(EnumType.STRING)
    @Column(length = 20, name="role_name")
    private AppRole roleName;

    public Role(Integer roleId) {
        this.roleId = roleId;
    }
}
