package com.k_plus.internship.UserPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @JsonProperty(value = "first_name", required = true)
    private String firstName;

    @JsonProperty(value = "last_name", required = true)
    private String lastName;

    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "password", required = true)
    private String password;

    @JsonProperty(value = "confirm_password", required = true)
    private String confirmPassword;
}
