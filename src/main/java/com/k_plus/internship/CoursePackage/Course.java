package com.k_plus.internship.CoursePackage;

import com.k_plus.internship.ArticlePackage.Article;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.UserPackage.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    private UUID id;

    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id")
    )
    private Collection<User> users;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Testing> testings;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Article> articles;
}