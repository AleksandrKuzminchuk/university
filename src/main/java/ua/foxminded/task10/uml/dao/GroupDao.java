package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.organization.Group;
import ua.foxminded.task10.uml.model.people.Student;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends CrudRepository<Group, Integer>{

    void saveAll(List<Group> groups);

    Optional<Group> findByGroupName(String groupName);

    void updateStudent(Group group);

    void assignStudentToGroup(Student student, Group group);
}

