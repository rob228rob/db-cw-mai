-- changeset rob229rob:009_question_owner_table
CREATE TABLE IF NOT EXISTS confirmed_answers
(
    id        UUID PRIMARY KEY,
    question_id UUID NOT NULL,
    answer_id UUID NOT NULL,
    FOREIGN KEY (answer_id) REFERENCES lawyer_answers (id) ON DELETE SET NULL,
    FOREIGN KEY (question_id) REFERENCES user_questions (id) ON DELETE SET NULL
);
