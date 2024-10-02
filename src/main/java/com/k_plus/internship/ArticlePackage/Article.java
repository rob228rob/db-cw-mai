package com.k_plus.internship.ArticlePackage;

import com.k_plus.internship.CoursePackage.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    private UUID id;

    private String title;

    private String author;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
}
