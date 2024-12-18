-- changeset rob229rob:006_create_user_questions_table
CREATE TABLE IF NOT EXISTS user_questions
(
    id            UUID PRIMARY KEY,
    user_id       UUID      NOT NULL,
    question_title VARCHAR(255),
    answered BOOLEAN DEFAULT FALSE,
    question_text TEXT,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );
