package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

public interface TeacherService extends CrudRepositoryService<Teacher, Integer>{

    void saveAll(List<Teacher> teachers);

    Teacher update(Teacher teacher);

    void addSubject(Teacher teacher, Subject subject);

    void addSubjects(Teacher teacher, List<Subject> subjects);

    List<Teacher> findByNameOrSurname(Teacher teacher);

    List<Subject> findSubjects(Integer teacherId);

    void updateSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId);

    void deleteSubject(Integer teacherId, Integer subjectId);
}
