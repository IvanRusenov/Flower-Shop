package com.ivan.Flowers.Shop.models.dtos;

import com.ivan.Flowers.Shop.enums.RoleType;
import com.ivan.Flowers.Shop.models.entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditUserDTO {

    private long id;
    @Size(min = 2, max = 50, message = "Username length must be between 2 and 50 symbols!")
    private String username;
    @Email(message = "Enter valid email!")
    @NotBlank(message = "Email cannot be empty!")
    private String email;
    @Size(min = 2, max = 50, message = "Shipping address length must be between 2 and 250 symbols!")
    private String shippingAddress;
    private RoleType role;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @Size(min = 2, max = 50, message = "Username length must be between 2 and 50 symbols!") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 2, max = 50, message = "Username length must be between 2 and 50 symbols!") String username) {
        this.username = username;
    }

    public @Email(message = "Enter valid email!") @NotBlank(message = "Email cannot be empty!") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Enter valid email!") @NotBlank(message = "Email cannot be empty!") String email) {
        this.email = email;
    }

    public @Size(min = 2, max = 50, message = "Shipping address length must be between 2 and 250 symbols!") String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(@Size(min = 2, max = 50, message = "Shipping address length must be between 2 and 250 symbols!") String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
