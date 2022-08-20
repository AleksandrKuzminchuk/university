DROP TABLE IF EXISTS classrooms CASCADE;
CREATE TABLE public.classrooms
(
    classroom_id SERIAL PRIMARY KEY ,
    room_number integer UNIQUE NOT NULL
)
