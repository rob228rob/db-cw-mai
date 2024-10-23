package com.k_plus.internship.UserPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @JsonProperty(value = "first_name", required = true)
    @NotNull
    private String firstName;

    @JsonProperty(value = "last_name", required = true)
    @NotNull
    private String lastName;

    @Email
    @NotNull
    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "password", required = true)
    @NotNull
    private String password;

    @JsonProperty(value = "confirm_password", required = true)
    @NotNull
    private String confirmPassword;
}
