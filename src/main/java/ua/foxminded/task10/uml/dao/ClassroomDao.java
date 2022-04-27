package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.place.Classroom;

import java.util.List;
import java.util.Optional;

public interface ClassroomDao extends CrudRepository<Classroom, Integer>{

    void saveAll(List<Classroom> classrooms);

    void updateClassroom(Integer classNumber, Integer newClassNumber);
}
