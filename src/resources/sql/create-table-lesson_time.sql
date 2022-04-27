CREATE TABLE public.lesson_time
(
    lesson_time_id SERIAL PRIMARY KEY ,
    start_time TIME NOT NULL ,
    end_time TIME NOT NULL
)