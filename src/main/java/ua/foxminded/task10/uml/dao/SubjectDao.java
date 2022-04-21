package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDao extends CrudRepository<Subject, Integer>{

    void saveAll(List<Subject> students);

    Optional<Subject> findSubjectByName(String subjectName);

    void updateSubject(Subject subject);
}
