package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.ClassroomDTO;

import java.util.List;

public interface ClassroomService extends CrudRepositoryService<ClassroomDTO, Integer> {

    Integer saveAll(List<ClassroomDTO> classrooms);

    ClassroomDTO update(ClassroomDTO classroom);

    ClassroomDTO findByNumber(Integer classroomNumber);
}
