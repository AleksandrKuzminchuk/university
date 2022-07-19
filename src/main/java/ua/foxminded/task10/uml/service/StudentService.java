package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.StudentDTO;

import java.util.List;

public interface StudentService extends CrudRepositoryService<StudentDTO, Integer> {

    Integer saveAll(List<StudentDTO> students);

    List<StudentDTO> findByCourseNumber(Integer number);

    StudentDTO update (StudentDTO student);

    List<StudentDTO> findByGroupName(String groupName);

    void deleteByCourseNumber(Integer courseNumber);

    StudentDTO deleteGroup(Integer studentId);

    void deleteByGroupId(Integer id);

    List<StudentDTO> findByNameOrSurname(String firstName, String lastName);

    List<StudentDTO> findByGroupId(Integer id);

    Long countByGroupId(Integer id);

    Long countByCourse(Integer course);
}
