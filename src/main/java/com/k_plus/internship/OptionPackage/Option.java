package com.k_plus.internship.OptionPackage;

import com.k_plus.internship.QuestionPackage.Question;
import com.k_plus.internship.UserAnswerPackage.UserAnswer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Table(name = "options")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Option {

    @Id
    private UUID id;

    private String optionText;

    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "option")
    private List<UserAnswer> userAnswers;

}
