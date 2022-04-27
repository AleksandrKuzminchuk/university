CREATE TABLE public.day_schedule_lessons
(
    day_schedule_id INTEGER NOT NULL REFERENCES day_schedules(day_schedule_id),
    lesson_id INTEGER NOT NULL references lessons(lesson_id)
)