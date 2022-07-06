package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Group;

import java.util.List;

public interface GroupService extends CrudRepositoryService<Group, Integer>{

    void saveAll(List<Group> groups);

    Group findByName(Group group);

    Group update(Group group);
}
