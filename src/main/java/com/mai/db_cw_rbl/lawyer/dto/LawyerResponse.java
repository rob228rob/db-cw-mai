package com.mai.db_cw_rbl.lawyer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mai.db_cw_rbl.user.dto.UserResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LawyerResponse {

    @JsonProperty("lawyer_id")
    private UUID id;

    @JsonProperty("years_experience")
    private int yearsExperience;

    @JsonProperty("specialization_name")
    private String specializationName;

    @JsonProperty("licence_number")
    private String licenceNumber;

    @JsonProperty("user_data")
    private UserResponse userData;
}
