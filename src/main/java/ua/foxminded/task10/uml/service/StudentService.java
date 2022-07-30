package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.response.StudentUpdateResponse;

import java.util.List;

public interface StudentService extends CrudRepositoryService<StudentDTO, Integer> {

    void saveAll(List<StudentDTO> students);

    List<StudentDTO> findByCourseNumber(Integer number);

    void update (StudentDTO student);

    StudentUpdateResponse updateForm(Integer id);

    List<StudentDTO> findByGroupName(String groupName);

    void deleteByCourseNumber(Integer courseNumber);

    void deleteGroup(Integer studentId);

    void deleteByGroupId(Integer id);

    List<StudentDTO> findByNameOrSurname(String firstName, String lastName);

    List<StudentDTO> findByGroupId(Integer id);

    Long countByGroupId(Integer id);

    Long countByCourse(Integer course);
}
