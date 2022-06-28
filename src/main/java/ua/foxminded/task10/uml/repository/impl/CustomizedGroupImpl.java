package ua.foxminded.task10.uml.repository.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.repository.CustomizedGroup;

import javax.persistence.EntityManager;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomizedGroupImpl implements CustomizedGroup {

    EntityManager entityManager;
    @Override
    public void assignStudentToGroup(Student student, Group group) {
        requireNonNull(student);
        requireNonNull(group);
        log.info("ASSIGN {} TO {}", student, group);
        Group addGroup = entityManager.find(Group.class, group.getId());
        Student addStudent = entityManager.find(Student.class, student.getId());
        addGroup.getStudents().add(addStudent);
        addStudent.setGroup(addGroup);
        entityManager.merge(addGroup);
        log.info("ASSIGNED {} TO {} SUCCESSFULLY", student, group);
    }

    @Override
    public void assignStudentsToGroup(List<Student> students, Group group) {
        requireNonNull(students);
        requireNonNull(group);
        log.info("ASSIGN {} TO GROUP  - {}", students.size(), group);
        students.forEach(student -> assignStudentToGroup(student, group));
        log.info("ASSIGNED {} TO GROUP - {} SUCCESSFULLY", students.size(), group);
    }
}
