DROP TABLE IF EXISTS subjects CASCADE;
CREATE TABLE public.subjects
(
    subject_id SERIAL PRIMARY KEY ,
    subject_name varchar (200) UNIQUE NOT NULL
)
