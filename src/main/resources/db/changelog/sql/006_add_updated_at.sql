-- changeset rob229rob:006_add_updated_at
ALTER TABLE user_questions
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;