package com.k_plus.internship.CoursePackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseCourseHeaderDto {

    @JsonProperty(value = "id", required = true)
    private UUID id;

    @JsonProperty(value = "header", required = true)
    private String header;

    @JsonProperty(value = "display_order", required = true)
    private int displayOrder;
}
