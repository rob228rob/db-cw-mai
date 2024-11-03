-- changeset rob229rob:007_create_lawyer_answers_table
CREATE TABLE IF NOT EXISTS lawyer_answers
(
    id          UUID PRIMARY KEY,
    question_id UUID      NOT NULL,
    lawyer_id   UUID      NOT NULL,
    answer_text TEXT      NOT NULL,
    answered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES user_questions (id) ON DELETE CASCADE,
    FOREIGN KEY (lawyer_id) REFERENCES lawyers (id) ON DELETE CASCADE
    );
