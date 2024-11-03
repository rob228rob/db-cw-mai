-- changeset rob229rob:004_create_specializations_table
CREATE TABLE IF NOT EXISTS specializations
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE
    );
