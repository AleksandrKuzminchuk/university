CREATE TABLE public.week_schedules_day_schedules
(
    week_id INTEGER NOT NULL REFERENCES week_schedules(week_id),
    day_schedule_id INTEGER NOT NULL REFERENCES day_schedules(day_schedule_id)
)