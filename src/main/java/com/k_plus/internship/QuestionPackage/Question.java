package com.k_plus.internship.QuestionPackage;

import com.k_plus.internship.OptionPackage.Option;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.UserAnswerPackage.UserAnswer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    private UUID id;

    @Column(name = "question_text")
    private String questionText;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Testing testing;

    @OneToMany(mappedBy = "question")
    private List<Option> options;

    @OneToMany(mappedBy = "question")
    private List<UserAnswer> userAnswers;
}
