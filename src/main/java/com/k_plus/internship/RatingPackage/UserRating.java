package com.k_plus.internship.RatingPackage;

import com.k_plus.internship.CoursePackage.Course;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.UserPackage.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ratings")
public class UserRating {

    @Id
    private UUID id;

    @Column(name = "correct_count")
    private Integer correctCount;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Testing.class)
    private Testing testing;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Course.class)
    private Course course;
}
