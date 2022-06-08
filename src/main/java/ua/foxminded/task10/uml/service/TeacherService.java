package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService extends CrudRepositoryService<Teacher, Integer>{

    void saveAll(List<Teacher> teachers);

    void updateTeacher(Integer teacherId, Teacher teacher);

    void addTeacherToSubject(Teacher teacherId, Subject subjectId);

    void addTeacherToSubjects(Teacher teacherId, List<Subject> subjects);

    Teacher findTeacherByNameSurname(Teacher teacher);

    List<Subject> findSubjectsByTeacherId(Integer teacherId);

    void updateTheTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId);

    void deleteTheTeacherSubject(Integer teacherId, Integer subjectId);
}
