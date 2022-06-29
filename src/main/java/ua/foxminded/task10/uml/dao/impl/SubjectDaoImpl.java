package ua.foxminded.task10.uml.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.dao.SubjectDao;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubjectDaoImpl implements SubjectDao {

    SessionFactory sessionFactory;

    @Override
    public Optional<Subject> save(Subject subject) {
        requireNonNull(subject);
        log.info("SAVING... SUBJECT {}", subject);
        Session session = sessionFactory.getCurrentSession();
        session.persist(subject);
        log.info("SAVED {} SUCCESSFULLY", subject);
        return Optional.of(subject);
    }

    @Override
    public Optional<Subject> findById(Integer subjectId) {
        requireNonNull(subjectId);
        log.info("FINDING SUBJECT BY ID - {}", subjectId);
        Session session = sessionFactory.getCurrentSession();
        Subject result = session.find(Subject.class, subjectId);
        log.info("FOUND {} BY ID - {} SUCCESSFULLY", result, subjectId);
        return Optional.ofNullable(result);
    }

    @Override
    public List<Subject> findSubjectsByName(Subject subject) {
        requireNonNull(subject);
        log.info("FIND SUBJECTS BY NAME {}", subject.getName());
        final String FIND_BY_NAME = "SELECT s FROM Subject s WHERE s.id =:subjectName";
        Session session = sessionFactory.getCurrentSession();
        List<Subject> result = session.createQuery(FIND_BY_NAME, Subject.class).setParameter("subjectName", subject.getName()).getResultList();
        log.info("FOUND {} SUBJECTS BY NAME {}", result.size(), subject.getName());
        return result;
    }

    @Override
    public boolean existsById(Integer subjectId) {
        requireNonNull(subjectId);
        log.info("CHECKING... SUBJECT EXISTS BY ID - {}", subjectId);
        final String EXISTS_BY_ID = "SELECT COUNT(s) FROM Subject s WHERE s.id=:subjectId";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(EXISTS_BY_ID, Long.class).setParameter("subjectId", subjectId).uniqueResult();
        boolean exists = count != null && count > 0;
        log.info("SUBJECT BY ID - {} EXISTS - {}", subjectId, exists);
        return exists;
    }

    @Override
    public List<Subject> findAll() {
        log.info("FINDING... ALL SUBJECTS");
        final String FIND_ALL = "SELECT s FROM Subject s ORDER BY s.name";
        Session session = sessionFactory.getCurrentSession();
        List<Subject> subjects = session.createQuery(FIND_ALL, Subject.class).getResultList();
        log.info("FOUND ALL SUBJECTS - {} SUCCESSFULLY", subjects.size());
        return subjects;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT SUBJECTS");
        final String COUNT = "SELECT COUNT(s) FROM Subject s";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(COUNT, Long.class).uniqueResult();
        log.info("FOUND COUNT({}) SUBJECTS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer subjectId) {
        requireNonNull(subjectId);
        log.info("DELETING SUBJECT BY ID - {}", subjectId);
        final String DELETE_BY_ID = "DELETE FROM Subject s WHERE s.id =:subjectId";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_BY_ID).setParameter("subjectId", subjectId).executeUpdate();
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", subjectId);
    }

    @Override
    public void delete(Subject subject) {
        throw new NotImplementedException("The Method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING ALL SUBJECTS");
        final String DELETE_ALL = "DELETE FROM Subject";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_ALL).executeUpdate();
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void deleteTheSubjectTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        log.info("DELETE THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        Session session = sessionFactory.getCurrentSession();
        Subject subject = session.get(Subject.class, subjectId);
        Teacher teacherToRemove = session.get(Teacher.class, teacherId);
        subject.getTeachers().remove(teacherToRemove);
        teacherToRemove.getSubjects().remove(subject);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        requireNonNull(subjects);
        log.info("SAVING SUBJECTS {}", subjects.size());
        subjects.forEach(this::save);
        log.info("SAVED SUBJECTS {} SUCCESSFULLY", subjects.size());
    }

    @Override
    public void updateSubject(Integer subjectId, Subject subject) {
        requireNonNull(subject);
        requireNonNull(subjectId);
        log.info("UPDATING... SUBJECT BY ID - {}", subjectId);
        Session session = sessionFactory.getCurrentSession();
        session.get(Subject.class, subjectId).setName(subject.getName());
        log.info("UPDATED {} SUCCESSFULLY", subject);
    }

    @Override
    public void updateTheSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId) {
        requireNonNull(subjectId);
        requireNonNull(oldTeacherId);
        requireNonNull(newTeacherId);
        log.info("UPDATE THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
        this.deleteTheSubjectTeacher(subjectId, oldTeacherId);
        Teacher teacher = new Teacher();
        teacher.setId(newTeacherId);
        this.addSubjectToTeacher(new Subject(subjectId), teacher);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacherId);
    }

    @Override
    public List<Teacher> findTeachersBySubject(Integer subjectId) {
        requireNonNull(subjectId);
        log.info("FINDING TEACHERS BY SUBJECT ID - {}", subjectId);
        final String FIND_TEACHERS_BY_SUBJECT = "SELECT s FROM Subject s LEFT JOIN FETCH s.teachers WHERE s.id=:subjectId";
        Session session = sessionFactory.getCurrentSession();
        List<Teacher> teachers = session.createQuery(FIND_TEACHERS_BY_SUBJECT, Subject.class).
                setParameter("subjectId", subjectId).uniqueResult().getTeachers();
        log.info("FOUND {} TEACHERS BY SUBJECT ID - {}", teachers.size(), subjectId);
        return teachers;
    }

    @Override
    public void addSubjectToTeacher(Subject subject, Teacher teacher) {
        requireNonNull(subject);
        requireNonNull(teacher);
        log.info("ADD SUBJECT BY ID - {} TO TEACHER BY ID - {}", subject.getId(), teacher.getId());
        Session session = sessionFactory.getCurrentSession();
        session.get(Subject.class, subject.getId()).getTeachers().add(teacher);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subject.getName(), teacher.getId());
    }

    @Override
    public void addSubjectToTeachers(Subject subject, List<Teacher> teachers) {
        requireNonNull(subject);
        requireNonNull(teachers);
        log.info("ADD SUBJECT BY ID - {} TO TEACHERS - {}", subject.getId(), teachers.size());
        teachers.forEach(teacher -> addSubjectToTeacher(subject, teacher));
        log.info("ADDED SUBJECT BY ID - {} TO TEACHERS - {} SUCCESSFULLY", subject.getId(), teachers.size());
    }
}
