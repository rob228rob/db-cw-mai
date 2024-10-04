package com.k_plus.internship.ArticlePackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequestDto {

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "author", required = true)
    private String author;

    @JsonProperty(value = "content", required = true)
    private String content;
}