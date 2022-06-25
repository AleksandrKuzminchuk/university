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
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TeacherDaoImpl implements TeacherDao {

    SessionFactory sessionFactory;

    @Override
    public Optional<Teacher> save(Teacher teacher) {
        requireNonNull(teacher);
        log.info("SAVING {}", teacher);
        Session session = sessionFactory.getCurrentSession();
        session.persist(teacher);
        log.info("SAVED {} SUCCESSFULLY", teacher);
        return Optional.of(teacher);
    }

    @Override
    public Optional<Teacher> findById(Integer teacherId) {
        requireNonNull(teacherId);
        log.info("FINDING TEACHER BY ID - {}", teacherId);
        Session session = sessionFactory.getCurrentSession();
        Teacher result = session.get(Teacher.class, teacherId);
        log.info("FOUND {} BY ID SUCCESSFULLY", result);
        return Optional.ofNullable(result);
    }

    @Override
    public List<Teacher> findTeachersByNameOrSurname(Teacher teacher) {
        requireNonNull(teacher);
        log.info("FIND TEACHERS {}", teacher);
        final String FIND_BY_NAME_SURNAME = "FROM Teacher t WHERE t.firstName=:firstName OR t.lastName=:lastName";
        Session session = sessionFactory.getCurrentSession();
        Query<Teacher> query = session.createQuery(FIND_BY_NAME_SURNAME, Teacher.class);
        query.setParameter("firstName", teacher.getFirstName());
        query.setParameter("lastName", teacher.getLastName());
        List<Teacher> result = query.list();
        log.info("FOUND {} TEACHERS BY {}", result.size(), teacher);
        return result;
    }

    @Override
    public boolean existsById(Integer teacherId) {
        requireNonNull(teacherId);
        log.info("CHECKING... TEACHER EXISTS BY ID - {}", teacherId);
        final String EXISTS_BY_ID = "SELECT COUNT(t) FROM Teacher t WHERE t.id=:id";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(EXISTS_BY_ID, Long.class).setParameter("id", teacherId).uniqueResult();
        boolean exists = count != null && count > 0;
        log.info("TEACHER BY ID - {} EXISTS - {}", teacherId, exists);
        return exists;
    }

    @Override
    public List<Teacher> findAll() {
        log.info("FINDING ALL TEACHERS...");
        final String FIND_ALL = "SELECT t FROM Teacher t ORDER BY t.firstName, t.lastName";
        Session session = sessionFactory.getCurrentSession();
        List<Teacher> teachers = session.createQuery(FIND_ALL, Teacher.class).getResultList();
        log.info("FOUND ALL TEACHERS: {}", teachers.size());
        return teachers;
    }

    @Override
    public Long count() {
        log.info("FIND COUNT ALL TEACHERS...");
        final String COUNT = "SELECT COUNT(t) FROM Teacher t";
        Session session = sessionFactory.getCurrentSession();
        Long countTeachers = session.createQuery(COUNT, Long.class).uniqueResult();
        log.info("FOUND COUNT({}) TEACHERS SUCCESSFULLY", countTeachers);
        return countTeachers;
    }

    @Override
    public void deleteById(Integer teacherId) {
        requireNonNull(teacherId);
        log.info("DELETE TEACHER BY ID - {}", teacherId);
        final String DELETE_BY_ID = "DELETE FROM Teacher t WHERE t.id=:teacherId";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_BY_ID).setParameter("teacherId", teacherId).executeUpdate();
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
    }

    @Override
    public void delete(Teacher teacher) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETE ALL TEACHERS...");
        final String DELETE_ALL = "DELETE FROM Teacher";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_ALL, Teacher.class).executeUpdate();
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
    }

    @Override
    public void deleteTheTeacherSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        log.info("DELETE THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(Teacher.class, teacherId);
        Subject subjectToRemove = session.get(Subject.class, subjectId);
        teacher.getSubjects().remove(subjectToRemove);
        subjectToRemove.getTeachers().remove(teacher);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
    }

    @Override
    public void saveAll(List<Teacher> teachers) {
        requireNonNull(teachers);
        log.info("SAVING {} TEACHERS", teachers.size());
        teachers.forEach(this::save);
        log.info("SAVED {} TEACHERS SUCCESSFULLY", teachers.size());
    }

    @Override
    public void updateTeacher(Integer teacherId, Teacher teacher) {
        requireNonNull(teacher);
        requireNonNull(teacherId);
        log.info("UPDATING TEACHER BY ID - {}", teacherId);
        Session session = sessionFactory.getCurrentSession();
        Teacher teacherToBeUpdated = session.get(Teacher.class, teacherId);
        teacherToBeUpdated.setFirstName(teacher.getFirstName());
        teacherToBeUpdated.setLastName(teacher.getLastName());
        log.info("UPDATED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
    }

    @Override
    public void updateTheTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId) {
        requireNonNull(teacherId);
        requireNonNull(oldSubjectId);
        requireNonNull(newSubjectId);
        log.info("UPDATE THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubjectId);
        this.deleteTheTeacherSubject(teacherId, oldSubjectId);
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        this.addTeacherToSubject(teacher, new Subject(newSubjectId));
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, oldSubjectId, newSubjectId);
    }

    @Override
    public void addTeacherToSubject(Teacher teacher, Subject subject) {
        requireNonNull(teacher);
        requireNonNull(subject);
        log.info("ADDING... TEACHER ID - {} TO SUBJECT ID - {}", teacher.getId(), subject.getId());
        Session session = sessionFactory.getCurrentSession();
        session.get(Teacher.class, teacher.getId()).getSubjects().add(subject);
        log.info("ADDED TEACHER ID - {} TO SUBJECT ID - {} SUCCESSFULLY", teacher.getId(), subject.getId());
    }

    @Override
    public void addTeacherToSubjects(Teacher teacher, List<Subject> subjects) {
        requireNonNull(teacher);
        requireNonNull(subjects);
        log.info("ADDING... TEACHER ID - {} TO SUBJECTS {}", teacher, subjects.size());
        subjects.forEach(subject -> addTeacherToSubject(teacher, subject));
        log.info("ADDED TEACHER ID - {} TO SUBJECTS {} SUCCESSFULLY", teacher, subjects.size());
    }

    @Override
    public List<Subject> findSubjectsByTeacherId(Integer teacherId) {
        requireNonNull(teacherId);
        log.info("FIND SUBJECTS BY TEACHER ID - {}", teacherId);
        final String FIND_SUBJECTS_BY_TEACHER_ID = "SELECT t FROM Teacher t LEFT JOIN FETCH t.subjects WHERE t.id=:teacherId";
        Session session = sessionFactory.getCurrentSession();
        List<Subject> subjects = session.createQuery(FIND_SUBJECTS_BY_TEACHER_ID, Teacher.class).
                setParameter("teacherId", teacherId).uniqueResult().getSubjects();
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return subjects;
    }
}
