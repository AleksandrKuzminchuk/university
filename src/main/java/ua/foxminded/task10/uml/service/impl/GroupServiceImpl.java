package ua.foxminded.task10.uml.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.repository.GroupRepository;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.util.GlobalNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GroupServiceImpl implements GroupService {

    GroupRepository groupRepository;

    @Override
    public Group save(Group group) {
        requireNonNull(group);
        log.info("SAVING... {}", group);
        groupRepository.save(group);
        log.info("SAVED {} SUCCESSFULLY", group);
        return group;
    }

    @Override
    public Group findById(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("FINDING... GROUP BY ID - {}", groupId);
        Group result = groupRepository.findById(groupId).orElseThrow(() -> new GlobalNotFoundException(format("Can't find group by groupId- %d", groupId)));
        log.info("FOUND {} BY ID - {} SUCCESSFULLY", result, groupId);
        return result;
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
    public List<Group> findAll() {
        log.info("FINDING... ALL GROUPS");
        List<Group> result = groupRepository.findAll(Sort.by(Sort.Order.asc("name")));
        log.info("FOUND {} GROUPS", result.size());
        return result;
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
    public void delete(Group group) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL GROUPS");
        groupRepository.deleteAll();
        log.info("DELETED ALL GROUPS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Group> groups) {
        requireNonNull(groups);
        log.info("SAVING... {} GROUPS", groups.size());
        groupRepository.saveAll(groups);
        log.info("SAVED {} GROUPS SUCCESSFULLY", groups.size());
    }

    @Override
    public Group findByName(Group group) {
        requireNonNull(group);
        log.info("FINDING... GROUPS BY NAME - {}", group.getName());
        Group result = groupRepository.findOne(Example.of(group))
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find group by name - %s", group.getName())));
        log.info("FOUND {} BY NAME - {} SUCCESSFULLY", result, group.getName());
        return result;
    }

    @Override
    public Group update(Group group) {
        requireNonNull(group);
        requiredGroupExistence(group.getId());
        log.info("UPDATING... GROUP BY ID - {}", group.getId());
        Group updatedGroup = groupRepository.save(group);
        log.info("UPDATED GROUP BY ID - {} SUCCESSFULLY", group.getId());
        return updatedGroup;
    }

    private void requiredGroupExistence(Integer groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(format("Group by id - %d not exists", groupId));
        }
    }
}
