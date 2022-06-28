package ua.foxminded.task10.uml.repository;

import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

public interface CustomizedStudent {

    void deleteStudentsByGroupId(Integer groupId);

    void updateTheStudentGroup(Integer groupId, Integer studentId);

    void deleteTheStudentGroup(Integer studentId);

    List<Student> findStudentsByGroupName(Group group);

    List<Student> findStudentsByNameOrSurname(Student student);
}
