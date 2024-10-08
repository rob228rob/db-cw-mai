package com.k_plus.internship.CoursePackage;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class CourseResponseAdminDto {
    
    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "testing_ids")
    private List<UUID> testingIds;

    @JsonProperty(value = "article_ids")
    private List<UUID> articleIds;
}
