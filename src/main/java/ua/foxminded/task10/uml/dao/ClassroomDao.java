package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.Classroom;

import java.util.List;
import java.util.Optional;

public interface ClassroomDao extends CrudRepositoryDao<Classroom, Integer> {

    void saveAll(List<Classroom> classrooms);

    void updateClassroom(Integer classroomId, Classroom classroom);

    Optional<Classroom> findClassroomByNumber(Integer classroomNumber);
}
