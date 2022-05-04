package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.Student;

import java.util.List;

public interface StudentDao extends CrudRepositoryDao<Student, Integer> {

    void saveAll(List<Student> students);

    List<Student> findByCourseNumber(Integer courseNumber);

    void updateStudent(Student student);

    List<Student> findStudentsByGroupId(Integer groupId);
}
