package ua.foxminded.task10.uml.service;

import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;

import java.util.List;

public interface GroupService extends CrudRepositoryService<GroupDTO, Integer>{

    void saveAll(List<GroupDTO> groups);

    GroupDTO findByName(String groupName);

    void update(GroupDTO group);

    List<StudentDTO> findStudents(Integer groupId);
}
