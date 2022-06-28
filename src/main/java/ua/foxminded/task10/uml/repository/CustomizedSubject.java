package ua.foxminded.task10.uml.repository;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

public interface CustomizedSubject {

    void deleteTheSubjectTeacher(Integer subjectId, Integer teacherId);

    void updateTheSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId);

    List<Teacher> findTeachersBySubject(Integer subjectId);

    void addSubjectToTeacher(Subject subject, Teacher teacher);

    void addSubjectToTeachers(Subject subject, List<Teacher> teachers);

    List<Subject> findSubjectsByName(Subject subject);
}
