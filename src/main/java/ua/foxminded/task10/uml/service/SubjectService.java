package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

public interface SubjectService extends CrudRepositoryService<Subject, Integer>{

    void saveAll(List<Subject> subjects);

    Subject update(Subject subject);

    List<Teacher> findTeachersBySubject(Integer subjectId);

    Subject findByName(Subject subject);

    void addSubjectToTeacher(Subject subject, Teacher teacher);

    void addSubjectToTeachers(Subject subject, List<Teacher> teachers);

    void updateAtSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId);

    void deleteFromSubjectTeacher(Integer subjectId, Integer teacherId);
}
