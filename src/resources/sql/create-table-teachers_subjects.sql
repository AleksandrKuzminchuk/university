CREATE TABLE public.teachers_subjects
(
    teacher_id INTEGER NOT NULL REFERENCES teacher(teachers_id),
    subject_id INTEGER NOT NULL REFERENCES subject(subjects_id)
)
