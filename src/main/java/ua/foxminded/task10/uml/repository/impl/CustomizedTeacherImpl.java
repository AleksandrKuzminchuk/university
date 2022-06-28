package ua.foxminded.task10.uml.repository.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.repository.CustomizedTeacher;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomizedTeacherImpl implements CustomizedTeacher {

    EntityManager entityManager;

    @Override
    public void deleteTheTeacherSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        log.info("DELETE THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        Teacher teacher = entityManager.find(Teacher.class, teacherId);
        Subject subjectToRemove = entityManager.find(Subject.class, subjectId);
        teacher.getSubjects().remove(subjectToRemove);
        subjectToRemove.getTeachers().remove(teacher);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
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
        Teacher teacherToBeSave = entityManager.find(Teacher.class, teacher.getId());
        Subject subjectToBeSave = entityManager.find(Subject.class, subject.getId());
        teacherToBeSave.getSubjects().add(subjectToBeSave);
        entityManager.persist(teacherToBeSave);
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
        List<Subject> subjects = entityManager.createQuery(FIND_SUBJECTS_BY_TEACHER_ID, Teacher.class).
                setParameter("teacherId", teacherId).getSingleResult().getSubjects();
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return subjects;
    }

    @Override
    public List<Teacher> findTeachersByNameOrSurname(Teacher teacher) {
        requireNonNull(teacher);
        log.info("FIND TEACHERS {}", teacher);
        final String FIND_BY_NAME_SURNAME = "FROM Teacher t WHERE t.firstName=:firstName OR t.lastName=:lastName";
        TypedQuery<Teacher> query = entityManager.createQuery(FIND_BY_NAME_SURNAME, Teacher.class);
        query.setParameter("firstName", teacher.getFirstName());
        query.setParameter("lastName", teacher.getLastName());
        List<Teacher> result = query.getResultList();
        log.info("FOUND {} TEACHERS BY {}", result.size(), teacher);
        return result;
    }
}
