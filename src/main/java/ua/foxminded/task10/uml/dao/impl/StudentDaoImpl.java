package ua.foxminded.task10.uml.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.dao.GroupDao;
import ua.foxminded.task10.uml.dao.StudentDao;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentDaoImpl implements StudentDao {

    GroupDao groupDao;
    SessionFactory sessionFactory;

    @Override
    public Optional<Student> save(Student student) {
        requireNonNull(student);
        log.info("SAVING {}", student);
        Session session = sessionFactory.getCurrentSession();
        session.persist(student);
        log.info("{} SAVED SUCCESSFULLY", student);
        return Optional.of(student);
    }

    @Override
    public Optional<Student> findById(Integer studentId) {
        requireNonNull(studentId);
        log.info("FIND STUDENT BY ID - {}", studentId);
        final String FIND_BY_ID = "SELECT s FROM Student s LEFT JOIN FETCH s.group WHERE s.id=:studentId";
        Session session = sessionFactory.getCurrentSession();
        Query<Student> query = session.createQuery(FIND_BY_ID, Student.class).setParameter("studentId", studentId);
        Student result = query.uniqueResult();
        log.info("FOUND {} BY ID SUCCESSFULLY", result);
        return Optional.of(result);
    }


    @Override
    public List<Student> findStudentsByNameOrSurname(Student student) {
        requireNonNull(student);
        log.info("FIND STUDENTS BY NAME OR SURNAME");
        final String FIND_BY_NAME_SURNAME = "SELECT s FROM Student s LEFT JOIN FETCH s.group WHERE " +
                "s.firstName=:firstName OR s.lastName=:lastName ORDER BY s.firstName, s.lastName";
        Session session = sessionFactory.getCurrentSession();
        Query<Student> query = session.createQuery(FIND_BY_NAME_SURNAME, Student.class);
        query.setParameter("firstName", student.getFirstName());
        query.setParameter("lastName", student.getLastName());
        List<Student> result = query.getResultList();
        log.info("FOUND STUDENT {} BY NAME OR SURNAME SUCCESSFULLY", result.size());
        return result;
    }

    @Override
    public boolean existsById(Integer studentId) {
        requireNonNull(studentId);
        log.info("CHECKING... STUDENT EXISTS BY ID - {}", studentId);
        final String EXISTS_BY_ID = "SELECT COUNT(s) FROM Student s WHERE s.id=:studentId";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(EXISTS_BY_ID, Long.class).setParameter("studentId", studentId).uniqueResult();
        boolean exists = count != null && count > 0;
        log.info("STUDENT BY ID - {} EXISTS - {}", studentId, exists);
        return exists;
    }

    @Override
    public List<Student> findAll() {
        log.info("FIND ALL STUDENTS...");
        final String FIND_ALL = "SELECT s FROM Student s LEFT JOIN FETCH s.group ORDER BY s.firstName, s.lastName";
        Session session = sessionFactory.getCurrentSession();
        List<Student> students = session.createQuery(FIND_ALL, Student.class).getResultList();
        log.info("FOUND ALL STUDENTS: {}", students.size());
        return students;
    }

    @Override
    public Long count() {
        log.info("FIND COUNT ALL STUDENTS...");
        final String COUNT = "SELECT COUNT(s) FROM Student s";
        Session session = sessionFactory.getCurrentSession();
        Long countStudents = session.createQuery(COUNT, Long.class).uniqueResult();
        log.info("FOUND COUNT({}) STUDENTS SUCCESSFULLY", countStudents);
        return countStudents;
    }

    @Override
    public Long countByGroupId(Integer groupId) {
        log.info("FIND COUNT BY GROUP ID - {}", groupId);
        final String COUNT_BY_STUDENT_ID = "SELECT COUNT(s) FROM Student s WHERE s.group.id=:groupId";
        Session session = sessionFactory.getCurrentSession();
        Long result = session.createQuery(COUNT_BY_STUDENT_ID, Long.class).setParameter("groupId", groupId).uniqueResult();
        log.info("FOUND COUNT({}) BY GROUP ID - {}", result, groupId);
        return result;
    }

    @Override
    public void deleteById(Integer studentId) {
        requireNonNull(studentId);
        log.info("DELETE STUDENT BY ID - {}", studentId);
        final String DELETE_BY_ID = "DELETE FROM Student s WHERE s.id=:studentId";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_BY_ID).setParameter("studentId", studentId).executeUpdate();
        log.info("DELETED STUDENT BY ID - {} SUCCESSFULLY", studentId);
    }

    @Override
    public void deleteStudentsByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        log.info("DELETE STUDENTS BY COURSE NUMBER - {}", courseNumber);
        final String DELETE_BY_COURSE_NUMBER = "DELETE FROM Student s WHERE s.course=:courseNumber";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_BY_COURSE_NUMBER).setParameter("courseNumber", courseNumber).executeUpdate();
        log.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", courseNumber);
    }

    @Override
    public void delete(Student student) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETE ALL STUDENTS");
        final String DELETE_ALL = "DELETE FROM Student";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_ALL).executeUpdate();
        log.info("DELETED ALL STUDENTS SUCCESSFULLY");
    }

    @Override
    public List<Student> findStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        log.info("FIND STUDENTS BY GROUP ID - {}", groupId);
        final String FIND_STUDENTS_BY_GROUP_ID = "SELECT s FROM Student s LEFT JOIN FETCH s.group WHERE s.group.id=:groupId " +
                "ORDER BY s.firstName, s.lastName";
        Session session = sessionFactory.getCurrentSession();
        List<Student> students = session.createQuery(FIND_STUDENTS_BY_GROUP_ID, Student.class).setParameter("groupId", groupId).getResultList();
        log.info("FOUND {} STUDENTS BY GROUP ID - {}", students.size(), groupId);
        return students;
    }

    @Override
    public void saveAll(List<Student> students) {
        requireNonNull(students);
        log.info("SAVING {} STUDENTS", students.size());
        students.forEach(this::save);
        log.info("SAVED {} STUDENTS SUCCESSFULLY", students.size());
    }

    @Override
    public List<Student> findByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        log.info("FINDING STUDENTS BY COURSE NUMBER {}", courseNumber);
        final String FIND_STUDENTS_BY_COURSE_NUMBER = "SELECT s FROM Student s LEFT JOIN FETCH s.group " +
                "WHERE s.course=:courseNumber ORDER BY s.firstName, s.lastName";
        Session session = sessionFactory.getCurrentSession();
        List<Student> students = session.createQuery(FIND_STUDENTS_BY_COURSE_NUMBER, Student.class)
                .setParameter("courseNumber", courseNumber).getResultList();
        log.info("FOUND {} BY COURSE NUMBER - {}", students, courseNumber);
        return students;
    }

    @Override
    public void updateStudent(Integer studentId, Student updatedStudent) {
        requireNonNull(studentId);
        requireNonNull(updatedStudent);
        log.info("UPDATING... STUDENT BY ID - {}", studentId);
        Session session = sessionFactory.getCurrentSession();
        Student student = session.find(Student.class, studentId);
        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setCourse(updatedStudent.getCourse());
        log.info("UPDATED {} SUCCESSFULLY", updatedStudent);
    }

    @Override
    public void updateTheStudentGroup(Integer groupId, Integer studentId) {
        requireNonNull(groupId);
        requireNonNull(studentId);
        log.info("UPDATE THE STUDENTS' BY ID - {} GROUP BY ID - {}", studentId, groupId);
        this.deleteTheStudentGroup(studentId);
        Student student = new Student();
        student.setId(studentId);
        groupDao.assignStudentToGroup(student, new Group(groupId));
        log.info("UPDATED THE STUDENTS' BY ID - {} GROUP BY ID - {} SUCCESSFULLY", studentId, groupId);
    }

    @Override
    public void deleteTheStudentGroup(Integer studentId) {
        requireNonNull(studentId);
        log.info("DELETE THE STUDENTS' BY ID - {} GROUP", studentId);
        final String UPDATE_THE_STUDENT_GROUP = "UPDATE Student s SET s.group.id=:groupId WHERE s.id=:studentId";
        Session session = sessionFactory.getCurrentSession();
        Query<Student> query = session.createQuery(UPDATE_THE_STUDENT_GROUP);
        query.setParameter("groupId", null);
        query.setParameter("studentId", studentId);
        query.executeUpdate();
        log.info("DELETED THE STUDENTS' BY ID - {} GROUP", studentId);
    }

    @Override
    public void deleteStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        log.info("DELETE STUDENTS BY GROUP ID - {}", groupId);
        final String DELETE_BY_GROUP_NAME = "UPDATE Student s SET s.group.id=:deleteGroupId WHERE s.group.id=:groupId";
        Session session = sessionFactory.getCurrentSession();
        Query<Student> query = session.createQuery(DELETE_BY_GROUP_NAME);
        query.setParameter("deleteGroupId", null);
        query.setParameter("groupId", groupId);
        query.executeUpdate();
        log.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", groupId);
    }

    @Override
    public List<Student> findStudentsByGroupName(Group group) {
        requireNonNull(group.getName());
        log.info("FINDING STUDENTS FROM GROUP NAME - {}", group.getName());
        final String FIND_STUDENTS_BY_GROUP_ID = "SELECT s FROM Student s LEFT JOIN FETCH s.group " +
                "WHERE upper(s.group.name)=:groupName ORDER BY s.firstName, s.lastName";
        Session session = sessionFactory.getCurrentSession();
        List<Student> students = session.createQuery(FIND_STUDENTS_BY_GROUP_ID, Student.class)
                .setParameter("groupName", group.getName()).getResultList();
        log.info("FOUND {} FROM GROUP NAME - {}", students.size(), group.getName());
        return students;
    }
}
