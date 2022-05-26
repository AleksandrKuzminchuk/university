package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

public interface TeacherService extends CrudRepositoryService<Teacher, Integer>{

    void saveAll(List<Teacher> teachers);

    void updateTeacher(Teacher teacher);

    void addTeacherToSubject(Teacher teacherId, Subject subjectId);

    void addTeacherToSubjects(Teacher teacherId, List<Subject> subjects);
}
