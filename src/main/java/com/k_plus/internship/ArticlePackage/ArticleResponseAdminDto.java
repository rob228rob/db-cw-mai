package com.k_plus.internship.ArticlePackage;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleResponseAdminDto {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "author")
    private String author;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "course_id")
    private UUID courseId;
}
