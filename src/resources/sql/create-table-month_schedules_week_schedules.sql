CREATE TABLE public.month_schedules_week_schedules
(
    month_id INTEGER NOT NULL REFERENCES month_schedules(month_id),
    week_id INTEGER NOT NULL REFERENCES week_schedules(week_id)
)