package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;
import java.util.Optional;

public interface GroupService extends CrudRepositoryService<Group, Integer>{

    void saveAll(List<Group> groups);

    Group findGroupByName(Group group);

    void updateGroup(Integer groupId, Group group);
}
