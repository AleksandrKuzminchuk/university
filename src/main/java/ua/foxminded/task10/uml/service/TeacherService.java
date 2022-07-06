package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

public interface TeacherService extends CrudRepositoryService<Teacher, Integer>{

    void saveAll(List<Teacher> teachers);

    Teacher update(Teacher teacher);

    void addTeacherToSubject(Teacher teacher, Subject subject);

    void addTeacherToSubjects(Teacher teacher, List<Subject> subjects);

    List<Teacher> findTeachersByNameOrSurname(Teacher teacher);

    List<Subject> findSubjectsByTeacher(Integer teacherId);

    void updateAtTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId);

    void deleteFromTeacherSubject(Integer teacherId, Integer subjectId);
}
