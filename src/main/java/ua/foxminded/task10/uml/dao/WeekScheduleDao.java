package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.schedule.DaySchedule;
import ua.foxminded.task10.uml.model.schedule.WeekSchedule;

import java.util.List;

public interface WeekScheduleDao extends CrudRepository<WeekSchedule, Integer>{

    void saveAll(List<WeekSchedule> weekSchedules);

    void addDayScheduleToWeek(WeekSchedule weekSchedule, DaySchedule daySchedules);

    void addDaySchedulesToWeek(WeekSchedule weekSchedule, List<DaySchedule> daySchedules);

    void update(Integer newWeekNumber, Integer weekId);

}
