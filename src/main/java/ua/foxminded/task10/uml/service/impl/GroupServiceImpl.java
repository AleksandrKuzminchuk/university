package ua.foxminded.task10.uml.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.repository.GroupRepository;
import ua.foxminded.task10.uml.repository.StudentRepository;
import ua.foxminded.task10.uml.service.GroupService;

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
    StudentRepository studentRepository;

    @Override
    public Group save(Group group) {
        requireNonNull(group);
        log.info("SAVING... {}", group);
        Group result = groupRepository.save(group);
        log.info("SAVED {} SUCCESSFULLY", group);
        return result;
    }

    @Override
    public Group findById(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("FINDING... GROUP BY ID - {}", groupId);
        Group result = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException(format("Can't find group by groupId- %d", groupId)));
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
        List<Group> result = groupRepository.findAll();
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
    public List<Group> findGroupsByName(String groupName) {
        requireNonNull(groupName);
        log.info("FINDING... GROUPS BY NAME - {}", groupName);
        List<Group> result = groupRepository.findGroupsByNameOrderByName(groupName);
        log.info("FOUND {} BY NAME - {} SUCCESSFULLY", result.size(), groupName);
        return result;
    }

    @Override
    public void updateGroup(Integer groupId, Group group) {
        requireNonNull(group);
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("UPDATING... GROUP BY ID - {}", groupId);
        group.setId(groupId);
        groupRepository.save(group);
        log.info("UPDATED GROUP BY ID - {} SUCCESSFULLY", groupId);
    }

    @Override
    public void assignStudentToGroup(Student student, Group group) {
        requireNonNull(student);
        requireNonNull(group);
        requiredStudentExistence(student.getId());
        requiredGroupExistence(group.getId());
        log.info("ASSIGNING STUDENT BY ID - {} TO GROUP BY ID- {}", student, group);
        groupRepository.assignStudentToGroup(student, group);
        log.info("ASSIGNED STUDENT BY ID - {} TO GROUP BY ID - {} SUCCESSFULLY", student, group);
    }

    @Override
    public void assignStudentsToGroup(List<Student> students, Group group) {
        requireNonNull(students);
        requireNonNull(group);
        requiredGroupExistence(group.getId());
        students.forEach(student -> requiredStudentExistence(student.getId()));
        log.info("ASSIGNING STUDENTS {} TO GROUP BY ID - {}", students.size(), group);
        groupRepository.assignStudentsToGroup(students, group);
        log.info("ASSIGNED STUDENTS {} TO GROUP BY ID - {} SUCCESSFULLY", students.size(), group);
    }

    private void requiredStudentExistence(Integer studentId){
        if (!studentRepository.existsById(studentId)){
            throw new NotFoundException(format("Student by id- %d not exists", studentId));
        }
    }

    private void requiredGroupExistence(Integer groupId){
        if (!existsById(groupId)){
            throw new NotFoundException(format("Group by id - %d not exists", groupId));
        }
    }
}
