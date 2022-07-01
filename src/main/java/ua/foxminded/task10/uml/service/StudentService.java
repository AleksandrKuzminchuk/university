package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

public interface StudentService extends CrudRepositoryService<Student, Integer> {

    void saveAll(List<Student> students);

    List<Student> findByCourseNumber(Integer number);

    void update ( Student student);

    List<Student> findByGroupName(Group group);

    void deleteByCourseNumber(Integer courseNumber);

    Student deleteFromGroupByStudentId(Integer studentId);

    void deleteByGroupId(Integer id);

    List<Student> findByNameOrSurname(Student student);

    List<Student> findByGroupId(Integer id);

    Long countByGroupId(Integer id);
}
