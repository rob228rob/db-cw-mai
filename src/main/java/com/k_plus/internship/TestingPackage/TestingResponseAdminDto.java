package com.k_plus.internship.TestingPackage;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestingResponseAdminDto {
    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "display_order")
    private int displayOrder;

    @JsonProperty(value = "course_id")
    private UUID courseId;

    @JsonProperty(value = "question_ids")
    private List<UUID> questionIds;
}

