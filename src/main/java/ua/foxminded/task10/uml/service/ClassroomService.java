package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Classroom;

import java.util.List;

public interface ClassroomService extends CrudRepositoryService<Classroom, Integer> {

    void saveAll(List<Classroom> classrooms);

    Classroom update(Classroom classroom);

    Classroom findByNumber(Classroom classroom);
}
