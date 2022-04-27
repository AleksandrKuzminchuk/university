CREATE TABLE public.schedules_month_schedules
(
    schedule_id INTEGER NOT NULL REFERENCES schedule(schedule_id),
    month_id INTEGER NOT NULL REFERENCES month_schedules(month_id)
)