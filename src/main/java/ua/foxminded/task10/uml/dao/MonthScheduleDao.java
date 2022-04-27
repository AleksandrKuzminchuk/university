package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.dao.CrudRepository;
import ua.foxminded.task10.uml.model.people.Student;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.schedule.MonthSchedule;
import ua.foxminded.task10.uml.model.schedule.WeekSchedule;

import java.util.List;
import java.util.Optional;

public interface MonthScheduleDao extends CrudRepository<MonthSchedule, Integer> {

    void saveAll(List<MonthSchedule> monthSchedules);

    void update(String newMonthName, Integer monthId);

    void addWeekScheduleToMonth(MonthSchedule monthSchedule, WeekSchedule weekSchedules);

    void addWeekSchedulesToMonth(MonthSchedule monthSchedule, List<WeekSchedule> weekSchedules);

    Optional<MonthSchedule> findByMonth(String month);

    Optional<MonthSchedule> findTeacherMonthSchedule(Teacher teacher, String month);

    Optional<MonthSchedule> findStudentMonthSchedule(Student student, String month);
}
