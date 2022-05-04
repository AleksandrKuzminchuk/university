package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Student;

import java.util.List;

public interface StudentService extends CrudRepositoryService<Student, Integer> {

    void saveAll(List<Student> students);

    List<Student> findByCourseNumber(Integer courseNumber);

    void updateStudent(Student student);

    List<Student> findStudentsByGroupId(Integer groupId);
}
