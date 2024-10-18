package com.k_plus.internship.UserAnswerPackage;

import com.k_plus.internship.OptionPackage.Option;
import com.k_plus.internship.QuestionPackage.Question;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.UserPackage.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_answers")
@Entity
public class UserAnswer {

    @Id
    private UUID id;

    @Column(name = "correct", columnDefinition = "boolean default false")
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Testing testing;

}