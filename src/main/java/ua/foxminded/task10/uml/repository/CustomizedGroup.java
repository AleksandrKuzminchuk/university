package ua.foxminded.task10.uml.repository;

import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

public interface CustomizedGroup {

    void assignStudentToGroup(Student student, Group group);

    void assignStudentsToGroup(List<Student> students, Group group);
}
