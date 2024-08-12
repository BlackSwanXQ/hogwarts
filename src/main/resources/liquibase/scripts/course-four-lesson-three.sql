-- liquibase formatted sql

-- changeset black2swan:1
CREATE INDEX student_name_index ON student (name);

--changeset black2swan:2
CREATE INDEX faculty_index ON faculty (name, color);




