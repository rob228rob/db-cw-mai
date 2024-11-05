-- Создание таблицы roles
CREATE TABLE IF NOT EXISTS roles
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    );
-- Создание таблицы specializations
CREATE TABLE IF NOT EXISTS specializations
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE
    );
-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users
(
    id            UUID PRIMARY KEY,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    enabled       BOOLEAN   DEFAULT TRUE,
    token_expired BOOLEAN   DEFAULT FALSE,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Создание таблицы users_roles (отношение многие-ко-многим)
CREATE TABLE IF NOT EXISTS users_roles
(
    user_id UUID NOT NULL,
    role_id INT  NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
    );

-- Создание таблицы lawyers
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

-- Создание таблицы user_questions
CREATE TABLE IF NOT EXISTS user_questions
(
    id            UUID PRIMARY KEY,
    user_id       UUID      NOT NULL,
    question_title VARCHAR(255) NOT NULL,
    question_text TEXT      NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

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

-- Создание таблицы lawyer_ratings
CREATE TABLE IF NOT EXISTS lawyer_ratings
(
    id        UUID PRIMARY KEY,
    user_id   UUID NOT NULL,
    lawyer_id UUID NOT NULL,
    rating    INT  NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment   TEXT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (lawyer_id) REFERENCES lawyers (id) ON DELETE CASCADE
    );

-- changeset rob229rob:009_question_owner_table
CREATE TABLE IF NOT EXISTS confirmed_answers
(
    id        UUID PRIMARY KEY,
    question_id   UUID NOT NULL,
    answer_id UUID NOT NULL,
    FOREIGN KEY (question_id) REFERENCES user_questions (id) ON DELETE SET NULL,
    FOREIGN KEY (answer_id) REFERENCES lawyer_answers (id) ON DELETE SET NULL
    );
