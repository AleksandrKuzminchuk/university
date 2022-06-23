package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.model.Group;

import java.util.List;
import java.util.Optional;

public interface ClassroomDao extends CrudRepositoryDao<Classroom, Integer> {

    void saveAll(List<Classroom> classrooms);

    void updateClassroom(Integer classroomId, Classroom classroom);

    List<Classroom> findClassroomsByNumber(Integer classroomNumber);
}
