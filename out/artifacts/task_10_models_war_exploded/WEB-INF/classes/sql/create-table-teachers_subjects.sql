CREATE TABLE public.teachers_subjects
(
    teacher_id INTEGER NOT NULL REFERENCES teachers (teacher_id) ON DELETE CASCADE,
    subject_id INTEGER NOT NULL REFERENCES subjects (subject_id) ON DELETE CASCADE
)
