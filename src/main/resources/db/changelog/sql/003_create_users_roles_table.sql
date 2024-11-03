-- changeset rob229rob:003_create_users_roles_table
CREATE TABLE IF NOT EXISTS users_roles
(
    user_id UUID NOT NULL,
    role_id INT  NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
    );
