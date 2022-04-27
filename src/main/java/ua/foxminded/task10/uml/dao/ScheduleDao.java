package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.schedule.MonthSchedule;
import ua.foxminded.task10.uml.model.schedule.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleDao extends CrudRepository<Schedule, Integer>{

    void saveAll(List<Schedule> schedules);

    void addMonthToSchedule(Schedule schedule, MonthSchedule monthSchedules);

    void addMonthsToSchedule(Schedule schedule, List<MonthSchedule> monthSchedules);

    Optional<Schedule> findByYear(Integer year);

    void update(Integer newYear, Integer scheduleId);
}
