package com.k_plus.internship.TestingPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TestingRequestDto {
    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "description", required = true)
    private String description;

    @JsonProperty(value = "display_order", required = true)
    private int displayOrder;

    @JsonProperty(value = "course_id", required = true)
    private UUID courseId;
}

