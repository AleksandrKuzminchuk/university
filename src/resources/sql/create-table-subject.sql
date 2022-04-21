CREATE TABLE public.subject
    (
        subject_id SERIAL PRIMARY KEY ,
        subject_name varchar (200) NOT NULL ,
        teacher_id integer NOT NULL,
        group_id integer
)