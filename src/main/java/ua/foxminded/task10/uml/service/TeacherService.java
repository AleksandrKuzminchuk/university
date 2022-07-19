package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

public interface TeacherService extends CrudRepositoryService<TeacherDTO, Integer>{

    Integer saveAll(List<TeacherDTO> teachers);

    TeacherDTO update(TeacherDTO teacher);

    void addSubject(Integer teacherId, Integer subjectId);

    void addSubjects(TeacherDTO teacher, List<SubjectDTO> subjects);

    List<TeacherDTO> findByNameOrSurname(String name, String surname);

    List<SubjectDTO> findSubjects(Integer teacherId);

    void updateSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId);

    void deleteSubject(Integer teacherId, Integer subjectId);
}
