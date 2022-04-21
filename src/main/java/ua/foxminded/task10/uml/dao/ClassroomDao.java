package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.place.Classroom;

import java.util.List;
import java.util.Optional;

public interface ClassroomDao extends CrudRepository<Classroom, Integer>{

    void saveAll(List<Classroom> classrooms);

    Optional<Classroom> findClassroomByName(String classroom);

    void updateClassroom(Classroom student);
}
