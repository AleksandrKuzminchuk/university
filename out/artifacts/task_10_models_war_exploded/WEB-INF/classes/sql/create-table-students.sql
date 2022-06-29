CREATE TABLE public.students
(
    student_id SERIAL PRIMARY KEY,
    first_name varchar(100) NOT NULL,
    last_name  varchar(100) NOT NULL,
    course     integer,
    group_id   integer,
    CONSTRAINT fk FOREIGN KEY (group_id) REFERENCES groups ON DELETE SET NULL
)
