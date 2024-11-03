-- changeset rob229rob:005_create_lawyers_table
CREATE TABLE IF NOT EXISTS lawyers
(
    id                UUID PRIMARY KEY,
    user_id           UUID NOT NULL,
    specialization_id UUID NOT NULL,
    years_experience  INT,
    licence_number    VARCHAR(64) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (specialization_id) REFERENCES specializations (id) ON DELETE SET NULL
    );
