package ua.foxminded.task10.uml.repository.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.repository.CustomizedStudent;
import ua.foxminded.task10.uml.repository.GroupRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomizedStudentImpl implements CustomizedStudent {

    EntityManager entityManager;

    GroupRepository groupRepository;
    @Override
    public void deleteStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        log.info("DELETE STUDENTS BY GROUP ID - {}", groupId);
        final String DELETE_BY_GROUP_NAME = "UPDATE Student s SET s.group.id=:deleteGroupId WHERE s.group.id=:groupId";
        Query query = entityManager.createQuery(DELETE_BY_GROUP_NAME);
        query.setParameter("deleteGroupId", null);
        query.setParameter("groupId", groupId);
        query.executeUpdate();
        log.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", groupId);
    }

    @Override
    public void updateTheStudentGroup(Integer groupId, Integer studentId) {
        requireNonNull(groupId);
        requireNonNull(studentId);
        log.info("UPDATE THE STUDENTS' BY ID - {} GROUP BY ID - {}", studentId, groupId);
        this.deleteTheStudentGroup(studentId);
        Student student = new Student();
        student.setId(studentId);
        groupRepository.assignStudentToGroup(student, new Group(groupId));
        log.info("UPDATED THE STUDENTS' BY ID - {} GROUP BY ID - {} SUCCESSFULLY", studentId, groupId);
    }

    @Override
    public void deleteTheStudentGroup(Integer studentId) {
        requireNonNull(studentId);
        log.info("DELETE THE STUDENTS' BY ID - {} GROUP", studentId);
        final String UPDATE_THE_STUDENT_GROUP = "UPDATE Student s SET s.group.id=:groupId WHERE s.id=:studentId";
        Query query = entityManager.createQuery(UPDATE_THE_STUDENT_GROUP);
        query.setParameter("groupId", null);
        query.setParameter("studentId", studentId);
        query.executeUpdate();
        log.info("DELETED THE STUDENTS' BY ID - {} GROUP", studentId);
    }

    @Override
    public List<Student> findStudentsByGroupName(Group group) {
        requireNonNull(group.getName());
        log.info("FINDING STUDENTS FROM GROUP NAME - {}", group.getName());
        final String FIND_STUDENTS_BY_GROUP_ID = "SELECT s FROM Student s LEFT JOIN FETCH s.group " +
                "WHERE upper(s.group.name)=:groupName ORDER BY s.firstName, s.lastName";
        List<Student> students = entityManager.createQuery(FIND_STUDENTS_BY_GROUP_ID, Student.class)
                .setParameter("groupName", group.getName()).getResultList();
        log.info("FOUND {} FROM GROUP NAME - {}", students.size(), group.getName());
        return students;
    }

    @Override
    public List<Student> findStudentsByNameOrSurname(Student student) {
        requireNonNull(student);
        log.info("FIND STUDENTS BY NAME OR SURNAME");
        final String FIND_BY_NAME_SURNAME = "SELECT s FROM Student s LEFT JOIN FETCH s.group WHERE " +
                "s.firstName=:firstName OR s.lastName=:lastName ORDER BY s.firstName, s.lastName";
        TypedQuery<Student> query = entityManager.createQuery(FIND_BY_NAME_SURNAME, Student.class);
        query.setParameter("firstName", student.getFirstName());
        query.setParameter("lastName", student.getLastName());
        List<Student> result = query.getResultList();
        log.info("FOUND STUDENT {} BY NAME OR SURNAME SUCCESSFULLY", result.size());
        return result;
    }
}
