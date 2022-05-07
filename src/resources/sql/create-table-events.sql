CREATE TABLE public.event
(
    event_id SERIAL PRIMARY KEY ,
    date_time TIMESTAMP NULL ,
    subject_id INTEGER REFERENCES subjects(subject_id) ON DELETE SET NULL ,
    classroom_id INTEGER REFERENCES classrooms(classrooms_id) ON DELETE SET NULL ,
    teacher_id INTEGER REFERENCES teachers(teacher_id) ON DELETE SET NULL ,
    group_id INTEGER REFERENCES groups(group_id) ON DELETE SET NULL
)
