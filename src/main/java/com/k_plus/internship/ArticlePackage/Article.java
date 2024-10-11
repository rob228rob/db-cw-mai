package com.k_plus.internship.ArticlePackage;

import com.k_plus.internship.CoursePackage.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.naming.Name;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "display_order")
    private int displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
}
