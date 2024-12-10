-- changeset rob229rob:008_create_lawyer_ratings_table
CREATE TABLE IF NOT EXISTS lawyer_ratings
(
    id        UUID PRIMARY KEY,
    lawyer_id UUID NOT NULL,
    question_id UUID NOT NULL,
    rating    INT  NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment   TEXT,
    FOREIGN KEY (lawyer_id) REFERENCES lawyers (id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES user_questions (id) ON DELETE CASCADE
    );
