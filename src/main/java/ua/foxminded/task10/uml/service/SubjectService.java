package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Subject;

import java.util.List;

public interface SubjectService extends CrudRepositoryService<Subject, Integer>{

    void saveAll(List<Subject> subjects);

    void updateSubject(Subject subject);

    List<Subject> findTeacherSubjects(Integer teacherId);
}
