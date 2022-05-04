package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Subject;

import java.util.List;

public interface SubjectDao extends CrudRepositoryDao<Subject, Integer> {

    void saveAll(List<Subject> subjects);

    void updateSubject(Subject subject);

    List<Subject> findTeacherSubjects(Integer teacherId);
}
