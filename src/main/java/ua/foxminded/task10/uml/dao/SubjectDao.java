package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface SubjectDao extends CrudRepositoryDao<Subject, Integer> {

    void saveAll(List<Subject> subjects);

    void updateSubject(Integer subjectId, Subject subject);

    List<Teacher> findTeachersBySubject(Integer subjectId);

    Optional<Subject> findSubjectByName(Subject subject);

    void addSubjectToTeacher(Subject subjectId, Teacher teacherId);

    void addSubjectToTeachers(Subject subjectId, List<Teacher> teachers);

    void updateTheSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId);

    void deleteTheSubjectTeacher(Integer subjectId, Integer teacherId);
}
