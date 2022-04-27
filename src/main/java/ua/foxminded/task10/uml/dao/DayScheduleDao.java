package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Lesson;
import ua.foxminded.task10.uml.model.people.Student;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.schedule.DaySchedule;

import java.time.LocalDate;
import java.util.Optional;

public interface DayScheduleDao extends CrudRepository<DaySchedule, Integer>{

    Optional<DaySchedule> findByDate(LocalDate date);

    void addLessonToSchedule(Lesson lesson, DaySchedule daySchedule);

    Optional<DaySchedule> findStudentDaySchedule(Student student, LocalDate date);

    Optional<DaySchedule> findTeacherDaySchedule(Teacher teacher, LocalDate date);

    void update(LocalDate newDate, Integer dayId);


}
