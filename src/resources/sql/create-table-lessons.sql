CREATE TABLE public.lessons
(
    lesson_id INTEGER PRIMARY KEY ,
    subject_id INTEGER REFERENCES subjects(subject_id) ON DELETE SET NULL ,
    classroom_id INTEGER REFERENCES classrooms(classrooms_id) ON DELETE SET NULL ,
    teacher_id INTEGER REFERENCES teachers(teacher_id) ON DELETE SET NULL ,
    group_id INTEGER REFERENCES groups(group_id) ON DELETE SET NULL ,
    lesson_time_id INTEGER REFERENCES lesson_time(lesson_time_id) ON DELETE SET NULL
)