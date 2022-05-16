package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

public interface GroupService extends CrudRepositoryService<Group, Integer>{

    void saveAll(List<Group> groups);

    Group findByGroupName(String groupName);

    void updateGroup(Group group);

    void assignStudentToGroup(Student studentId, Group groupId);

    void assignStudentsToGroup(List<Student> students, Group groupId);
}
