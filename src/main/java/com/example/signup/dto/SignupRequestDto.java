package com.example.signup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDto {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is Required")
    @Email(message = "Please Enter valid email")
    private String email;
    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;
}
