package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

public interface GroupService extends CrudRepositoryService<Group, Integer>{

    void saveAll(List<Group> groups);

    List<Group> findGroupsByName(String groupName);

    void updateGroup(Integer groupId, Group group);

    void assignStudentToGroup(Student student, Group group);

    void assignStudentsToGroup(List<Student> students, Group group);
}
