package com.k_plus.internship.TestingPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestingResponseDto {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "question_ids")
    private List<UUID> questionIds;
}
