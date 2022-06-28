package ua.foxminded.task10.uml.repository.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.repository.CustomizedSubject;

import javax.persistence.EntityManager;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomizedSubjectImpl implements CustomizedSubject {

    EntityManager entityManager;
    @Override
    public void deleteTheSubjectTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        log.info("DELETE THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        Subject subject = entityManager.find(Subject.class, subjectId);
        Teacher teacherToRemove = entityManager.find(Teacher.class, teacherId);
        subject.getTeachers().remove(teacherToRemove);
        teacherToRemove.getSubjects().remove(subject);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
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
        List<Teacher> teachers = entityManager.createQuery(FIND_TEACHERS_BY_SUBJECT, Subject.class).
                setParameter("subjectId", subjectId).getSingleResult().getTeachers();
        log.info("FOUND {} TEACHERS BY SUBJECT ID - {}", teachers.size(), subjectId);
        return teachers;
    }

    @Override
    public void addSubjectToTeacher(Subject subject, Teacher teacher) {
        requireNonNull(subject);
        requireNonNull(teacher);
        log.info("ADD SUBJECT BY ID - {} TO TEACHER BY ID - {}", subject.getId(), teacher.getId());
        Teacher teacherToBeSave = entityManager.find(Teacher.class, teacher.getId());
        Subject subjectToBeSave = entityManager.find(Subject.class, subject.getId());
        subjectToBeSave.getTeachers().add(teacherToBeSave);
        entityManager.persist(subjectToBeSave);
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

    @Override
    public List<Subject> findSubjectsByName(Subject subject) {
        requireNonNull(subject);
        log.info("FIND SUBJECTS BY NAME {}", subject.getName());
        final String FIND_BY_NAME = "SELECT s FROM Subject s WHERE s.name =:subjectName";
        List<Subject> result = entityManager.createQuery(FIND_BY_NAME, Subject.class).setParameter("subjectName", subject.getName()).getResultList();
        log.info("FOUND {} SUBJECTS BY NAME {}", result.size(), subject.getName());
        return result;
    }
}
