package ua.foxminded.task10.uml.repository;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

public interface CustomizedTeacher {

    void deleteTheTeacherSubject(Integer teacherId, Integer subjectId);

    void updateTheTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId);

    void addTeacherToSubject(Teacher teacher, Subject subject);

    void addTeacherToSubjects(Teacher teacher, List<Subject> subjects);

    List<Subject> findSubjectsByTeacherId(Integer teacherId);

    List<Teacher> findTeachersByNameOrSurname(Teacher teacher);

}
