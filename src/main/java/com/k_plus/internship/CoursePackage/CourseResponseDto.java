package com.k_plus.internship.CoursePackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CourseResponseDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty("is_paid")
    private boolean isPaid;

    @JsonProperty(value = "testing_ids")
    private List<ResponseCourseHeaderDto> testingIds;

    @JsonProperty(value = "article_ids")
    private List<ResponseCourseHeaderDto> articleIds;
}
