-- changeset rob229rob:006_add_answered
ALTER TABLE user_questions
    ADD COLUMN answered BOOLEAN DEFAULT FALSE;