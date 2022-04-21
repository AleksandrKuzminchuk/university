package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.people.Student;
import ua.foxminded.task10.uml.model.people.Teacher;

import java.util.List;

public interface TeacherDao extends CrudRepository<Teacher, Integer> {

    void saveAll(List<Teacher> teachers);

    void updateTeacher(Teacher teacher);

}
