package com.service.notebook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest implements Serializable {
    @NotBlank()
    @Email(message = "must have correct format")
    private String email;
    @NotBlank()
    private String firstName;
    @NotBlank()
    private String lastName;
    @NotNull
    private Date birthday;
    private AddressDTO address;
    private String phoneNumber;
}
