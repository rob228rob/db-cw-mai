package com.k_plus.internship.QuestionPackage;

import java.util.List;
import java.util.UUID;

import com.k_plus.internship.OptionPackage.Option;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.UserAnswerPackage.UserAnswer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "question", cascade=CascadeType.ALL)
    private List<Option> options;

    @OneToMany(mappedBy = "question")
    private List<UserAnswer> userAnswers;
}
