package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.response.TeacherAddSubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherFindSubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherUpdateSubjectResponse;

import java.util.List;

public interface TeacherService extends CrudRepositoryService<TeacherDTO, Integer>{

    void saveAll(List<TeacherDTO> teachers);

    void update(TeacherDTO teacher);

    SubjectDTO addSubject(Integer teacherId, Integer subjectId);

    List<SubjectDTO> addSubjects(TeacherDTO teacher, List<SubjectDTO> subjects);

    List<TeacherDTO> findByNameOrSurname(String name, String surname);

    List<SubjectDTO> findSubjects(Integer teacherId);

    SubjectDTO updateSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId);

    void deleteSubject(Integer teacherId, Integer subjectId);

    TeacherFindSubjectResponse findSubjectsForm(Integer id);

    TeacherAddSubjectResponse addSubjectFrom(Integer id);

    TeacherUpdateSubjectResponse updateSubjectForm(Integer teacherId, Integer subjectId);
}
