package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.place.Classroom;

import java.util.List;

public interface ClassroomDao extends CrudRepositoryDao<Classroom, Integer> {

    void saveAll(List<Classroom> classrooms);

    void updateClassroom(Classroom classroom);
}
