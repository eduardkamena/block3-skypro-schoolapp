-- liquibase formatted sql

-- changeset ekamenskikh:1
CREATE INDEX student_name_index ON student (name);

-- changeset ekamenskikh:2
CREATE INDEX faculty_nc_index ON faculty (name, color);