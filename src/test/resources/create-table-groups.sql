DROP TABLE IF EXISTS groups CASCADE;
CREATE TABLE public.groups
(
    group_id SERIAL PRIMARY KEY ,
    group_name varchar(250) UNIQUE NOT NULL
)