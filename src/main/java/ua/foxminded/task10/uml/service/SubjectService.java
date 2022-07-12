package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

public interface SubjectService extends CrudRepositoryService<Subject, Integer>{

    void saveAll(List<Subject> subjects);

    Subject update(Subject subject);

    List<Teacher> findTeachers(Integer subjectId);

    Subject findByName(Subject subject);

    void addTeacher(Subject subject, Teacher teacher);

    void addTeachers(Subject subject, List<Teacher> teachers);

    void updateTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId);

    void deleteTeacher(Integer subjectId, Integer teacherId);
}
