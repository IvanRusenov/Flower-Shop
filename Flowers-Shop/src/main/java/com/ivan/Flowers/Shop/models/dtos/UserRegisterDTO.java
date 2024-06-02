package com.ivan.Flowers.Shop.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {

    @Size(min = 2, max = 50, message = "Username length must be between 2 and 50 symbols!")
    private String username;
    @Email(message = "Enter valid email!")
    @NotBlank(message = "Email cannot be empty!")
    private String email;
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 symbols!")
    private String password;
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 symbols!")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
