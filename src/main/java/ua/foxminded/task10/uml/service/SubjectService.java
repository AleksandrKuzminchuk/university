package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.util.List;

public interface SubjectService extends CrudRepositoryService<SubjectDTO, Integer>{

    Integer saveAll(List<SubjectDTO> subjectsDTO);

    SubjectDTO update(SubjectDTO subjectDTO);

    List<TeacherDTO> findTeachers(Integer subjectId);

    SubjectDTO findByName(String subjectName);

    void addTeacher(Integer subjectId, Integer teacherId);

    void addTeachers(SubjectDTO subjectDTO, List<TeacherDTO> teachersDTO);

    void updateTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId);

    void deleteTeacher(Integer subjectId, Integer teacherId);
}
