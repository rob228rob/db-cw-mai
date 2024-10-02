package com.k_plus.internship.CoursePackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CourseRequestDto {

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "description", required = true)
    private String description;

    @JsonProperty(value = "test_ids", required = false)
    private List<UUID> testIds = new ArrayList<>();

    @JsonProperty(value = "article_ids", required = false)
    private List<UUID> articleIds = new ArrayList<>();
}
