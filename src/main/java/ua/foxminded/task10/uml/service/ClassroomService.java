package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Classroom;

import java.util.List;
import java.util.Optional;

public interface ClassroomService extends CrudRepositoryService<Classroom, Integer> {

    void saveAll(List<Classroom> classrooms);

    void updateClassroom(Integer classroomId, Classroom classroom);

    Classroom findClassroomByNumber(Integer classroomNumber);
}
