package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Subject;
import ua.foxminded.task10.uml.model.people.Teacher;

import java.util.List;
import java.util.Optional;

public interface SubjectDao extends CrudRepository<Subject, Integer>{

    void saveAll(List<Subject> subjects);

    void updateSubject(String subjectName, String newSubjectName);

    List<Subject> findTeacherSubjects(Integer teacherId);
}
