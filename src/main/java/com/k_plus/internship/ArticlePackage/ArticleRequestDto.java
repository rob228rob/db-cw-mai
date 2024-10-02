package com.k_plus.internship.ArticlePackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequestDto {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "author")
    private String author;

    @JsonProperty(value = "content")
    private String content;
}
