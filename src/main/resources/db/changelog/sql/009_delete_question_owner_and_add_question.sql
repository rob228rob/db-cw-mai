-- changeset rob229rob:010_replace_question_owner_with_question_id
ALTER TABLE confirmed_answers
    DROP CONSTRAINT IF EXISTS confirmed_answers_question_owner_id_fkey;

ALTER TABLE confirmed_answers
    DROP COLUMN IF EXISTS question_owner_id;

ALTER TABLE confirmed_answers
    ADD COLUMN question_id UUID NOT NULL;

ALTER TABLE confirmed_answers
    ADD CONSTRAINT confirmed_answers_question_id_fkey
        FOREIGN KEY (question_id)
        REFERENCES user_questions (id)
        ON DELETE CASCADE;