-- changeset rob229rob:008_drop_user_id_and_add_question_id
ALTER TABLE lawyer_ratings
DROP CONSTRAINT IF EXISTS lawyer_ratings_user_id_fkey;

ALTER TABLE lawyer_ratings
DROP COLUMN IF EXISTS user_id;

ALTER TABLE lawyer_ratings
    ADD COLUMN question_id UUID NOT NULL;

ALTER TABLE lawyer_ratings
    ADD CONSTRAINT lawyer_ratings_question_id_fkey
        FOREIGN KEY (question_id)
            REFERENCES user_questions (id)
            ON DELETE CASCADE;