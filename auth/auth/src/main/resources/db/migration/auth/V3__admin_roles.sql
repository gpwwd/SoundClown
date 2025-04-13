ALTER TABLE auth.admin
    ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'ADMIN_BASIC';

UPDATE auth.admin
SET role = 'ADMIN_BASIC'
WHERE role IS NULL;

ALTER TABLE auth.admin
    ADD CONSTRAINT chk_admin_role
        CHECK (role IN ('ADMIN_BASIC', 'ADMIN_MANAGER', 'ADMIN_GOD'));

INSERT INTO auth.base_user (username, email, phone_number, password_hash)
VALUES ('superadmin', 'super@admin.com', NULL, 'hashed-password')
RETURNING id;

INSERT INTO auth.admin (id, role)
VALUES (1, 'ADMIN_GOD');