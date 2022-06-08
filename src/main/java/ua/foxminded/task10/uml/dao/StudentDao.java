package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao extends CrudRepositoryDao<Student, Integer> {

    void saveAll(List<Student> students);

    List<Student> findByCourseNumber(Integer courseNumber);

    void updateStudent(Integer studentId,Student updatedStudent);

    List<Student> findStudentsByGroupName(Group groupName);

    void deleteStudentsByCourseNumber(Integer courseNumber);

    void deleteStudentsByGroupId(Integer groupId);

    List<Student> findStudentsByNameOrSurname(Student student);

    void deleteTheStudentGroup(Integer studentId);

    void updateTheStudentGroup(Integer groupId, Integer studentId);

    List<Student> findStudentsByGroupId(Integer groupId);
 }
