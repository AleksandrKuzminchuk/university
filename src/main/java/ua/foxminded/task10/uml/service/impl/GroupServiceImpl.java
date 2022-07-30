package ua.foxminded.task10.uml.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.mapper.GroupMapper;
import ua.foxminded.task10.uml.dto.mapper.StudentMapper;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.repository.GroupRepository;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotFoundException;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotNullException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final StudentService studentService;

    @Override
    public GroupDTO save(GroupDTO groupDTO) {
        log.info("SAVING... {}", groupDTO);
        Group group = groupMapper.convertToGroup(groupDTO);
        groupRepository.save(group);
        GroupDTO savedGroupDTO = groupMapper.convertToGroupDTO(group);
        log.info("SAVED {} SUCCESSFULLY", savedGroupDTO);
        return savedGroupDTO;
    }

    @Override
    public GroupDTO findById(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("FINDING... GROUP BY ID - {}", groupId);
        Group result = groupRepository.findById(groupId).orElseThrow(() -> new GlobalNotFoundException(format("Can't find group by groupId- %d", groupId)));
        GroupDTO groupDTO = groupMapper.convertToGroupDTO(result);
        log.info("FOUND {} BY ID - {} SUCCESSFULLY", groupDTO, groupId);
        return groupDTO;
    }

    @Override
    public boolean existsById(Integer groupId) {
        requireNonNull(groupId);
        log.info("CHECKING... GROUP EXISTS BY ID - {}", groupId);
        boolean result = groupRepository.existsById(groupId);
        log.info("GROUP BY ID - {} EXISTS - {}", groupId, result);
        return result;
    }

    @Override
    public List<GroupDTO> findAll() {
        log.info("FINDING... ALL GROUPS");
        List<Group> result = groupRepository.findAll(Sort.by(Sort.Order.asc("name")));
        List<GroupDTO> groupsDTO = result.stream().map(groupMapper::convertToGroupDTO).collect(Collectors.toList());
        log.info("FOUND {} GROUPS", groupsDTO.size());
        return groupsDTO;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT GROUPS");
        Long result = groupRepository.count();
        log.info("FOUND COUNT({}) GROUPS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public void deleteById(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("DELETING... GROUP BY ID- {}", groupId);
        groupRepository.deleteById(groupId);
        log.info("DELETED GROUP BY ID - {} SUCCESSFULLY", groupId);
    }

    @Override
    public void delete(GroupDTO groupDTO) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL GROUPS");
        groupRepository.deleteAll();
        log.info("DELETED ALL GROUPS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<GroupDTO> groupsDTO) {
        requireNonNull(groupsDTO);
        log.info("SAVING... {} GROUPS", groupsDTO.size());
        List<Group> groups = groupsDTO.stream().map(groupMapper::convertToGroup).collect(Collectors.toList());
        groupRepository.saveAll(groups);
        log.info("SAVED {} GROUPS SUCCESSFULLY", groups.size());
    }

    @Override
    public GroupDTO findByName(String groupName) {
        log.info("FINDING... GROUPS BY NAME - {}", groupName);
        Group result = groupRepository.findByName(groupName).orElseThrow(() -> new GlobalNotFoundException(format("Can't find groupName by name - %s", groupName)));
        GroupDTO groupDTO = groupMapper.convertToGroupDTO(result);
        log.info("FOUND {} BY NAME - {} SUCCESSFULLY", groupDTO, groupName);
        return groupDTO;
    }

    @Override
    public void update(GroupDTO groupDTO) {
        requiredGroupExistence(groupDTO.getId());
        log.info("UPDATING... GROUP BY ID - {}", groupDTO.getId());
        Group group = groupMapper.convertToGroup(groupDTO);
        Group updatedGroup = groupRepository.save(group);
        groupMapper.convertToGroupDTO(updatedGroup);
        log.info("UPDATED GROUP BY ID - {} SUCCESSFULLY", groupDTO.getId());
    }

    @Override
    public List<StudentDTO> findStudents(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("FINDING STUDENTS BY GROUP ID - {}", groupId);
        List<StudentDTO> studentsDTO = studentService.findByGroupId(groupId);
        log.info("FOUND {} STUDENTS BY GROUP ID - {}", studentsDTO, groupId);
        return studentsDTO;
    }

    private void requiredGroupExistence(Integer groupId){
        if (!groupRepository.existsById(groupId)){
            throw new GlobalNotFoundException(format("Group by id - %d not exists", groupId));
        }
    }

    private void requireNonNull(Object o){
        if (o == null){
            throw new GlobalNotNullException("Can't be null " + o.getClass().getName());
        }
    }
}
