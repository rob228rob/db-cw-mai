package com.mai.db_cw_rbl.LawyerPackage.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import com.mai.db_cw_rbl.UserPackage.UserRegistrationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LawyerCreationRequest {

    @JsonProperty("user_data")
    @NotNull(message = "User data is mandatory")
    @Valid
    private UserRegistrationRequest userData;

    @JsonProperty("experience_years")
    @Min(value = 0, message = "Experience years cannot be negative")
    private int experienceYears;

    @JsonProperty("licence")
    @NotBlank(message = "Licence is mandatory")
    @Size(max = 50, message = "Licence must not exceed 50 characters")
    private String licence;

    @JsonProperty("specialization")
    @NotBlank(message = "Specialization is mandatory")
    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    // Копи-конструктор
    public LawyerCreationRequest(LawyerCreationRequest request) {
        this.userData = request.getUserData();
        this.experienceYears = request.getExperienceYears();
        this.licence = request.getLicence();
        this.specialization = request.getSpecialization();
    }
}

