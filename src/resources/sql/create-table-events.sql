CREATE TABLE public.events
(
    event_id     SERIAL PRIMARY KEY,
    date_time    TIMESTAMP DEFAULT current_timestamp,
    subject_id   INTEGER  ,
    classroom_id INTEGER  ,
    teacher_id   INTEGER  ,
    group_id     INTEGER  ,
    CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subjects (subject_id) ON DELETE SET NULL,
    CONSTRAINT fk_classroom FOREIGN KEY (classroom_id) REFERENCES classrooms (classroom_id) ON DELETE SET NULL ,
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE SET NULL ,
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES groups (group_id) ON DELETE SET NULL
)
