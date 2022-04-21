CREATE TABLE public.classroom
(
    classroom_id SERIAL PRIMARY KEY ,
    room_number varchar (200) NOT NULL,
    describe_classroom varchar (200) NOT NULL
)