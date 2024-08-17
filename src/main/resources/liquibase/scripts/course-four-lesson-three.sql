-- liquibase formatted sql

-- changeset black2swan:1
CREATE INDEX student_name_index ON student (name);

--changeset black2swan:2
CREATE INDEX faculty_index ON faculty (name, color);




-- UPDATE `databasechangelog` SET `MD5SUM`=`7:bde7b076d47aefe8278d126cde26b172` WHERE `ID`=`00000000000001`;