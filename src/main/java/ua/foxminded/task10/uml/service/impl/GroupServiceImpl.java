package ua.foxminded.task10.uml.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.task10.uml.dao.GroupDao;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class GroupServiceImpl implements GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupDao groupDao;
    private final StudentService studentService;

    public GroupServiceImpl(GroupDao groupDao, StudentService studentService) {
        this.groupDao = groupDao;
        this.studentService = studentService;
    }

    @Override
    public Group save(Group group) {
        requireNonNull(group);
        logger.info("SAVING... {}", group);
        Group result = groupDao.save(group).orElseThrow(() -> new NotFoundException(format("Can't save %s", group)));
        logger.info("SAVED {} SUCCESSFULLY", group);
        return result;
    }

    @Override
    public Group findById(Integer id) {
        requireNonNull(id);
        logger.info("FINDING... GROUP BY ID - {}", id);
        Group result = groupDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find group by id- %d", id)));
        logger.info("FOUND {} BY ID - {} SUCCESSFULLY", result, id);
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... GROUP EXISTS BY ID - {}", id);
        boolean result = groupDao.existsById(id);
        logger.info("GROUP BY ID - {} EXISTS - {}", id, result);
        return result;
    }

    @Override
    public List<Group> findAll() {
        logger.info("FINDING... ALL GROUPS");
        List<Group> result = groupDao.findAll();
        logger.info("FOUND {} GROUPS", result.size());
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT GROUPS");
        Long result = groupDao.count();
        logger.info("FOUND COUNT({}) GROUPS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETING... GROUP BY ID- {}", id);
        groupDao.deleteById(id);
        logger.info("DELETED GROUP BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Group group) {
        requireNonNull(group);
        logger.info("DELETING... {}", group);
        groupDao.delete(group);
        logger.info("DELETED {} SUCCESSFULLY", group);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL GROUPS");
        groupDao.deleteAll();
        logger.info("DELETED ALL GROUPS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Group> groups) {
        requireNonNull(groups);
        logger.info("SAVING... {} GROUPS", groups.size());
        groupDao.saveAll(groups);
        logger.info("SAVED {} GROUPS SUCCESSFULLY", groups.size());
    }

    @Override
    public Group findByGroupName(String groupName) {
        requireNonNull(groupName);
        logger.info("FINDING... GROUP BY GROUP NAME - {}", groupName);
        Group result = groupDao.findByGroupName(groupName).orElseThrow(() -> new NotFoundException(format("Can't find group by group name - %s", groupName)));
        logger.info("FOUND {} BY GROUP NAME - {} SUCCESSFULLY", result, groupName);
        return result;
    }

    @Override
    public void updateGroup(Group group) {
        requireNonNull(group);
        logger.info("UPDATING... {}", group);
        groupDao.updateGroup(group);
        logger.info("UPDATED {} SUCCESSFULLY", group);
    }

    @Override
    public void assignStudentToGroup(Integer studentId, Integer groupId) {
        requireNonNull(studentId);
        requireNonNull(groupId);
        requiredStudentExistence(studentId);
        requiredGroupExistence(groupId);
        logger.info("ASSIGNING STUDENT BY ID - {} TO GROUP BY ID- {}", studentId, groupId);
        groupDao.assignStudentToGroup(studentId, groupId);
        logger.info("ASSIGNED STUDENT BY ID - {} TO GROUP BY ID - {} SUCCESSFULLY", studentId, groupId);
    }

    @Override
    public void assignStudentsToGroup(List<Student> students, Integer groupId) {
        requireNonNull(students);
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        students.forEach(student -> requiredStudentExistence(student.getId()));
        logger.info("ASSIGNING STUDENTS {} TO GROUP BY ID - {}", students.size(), groupId);
        groupDao.assignStudentsToGroup(students, groupId);
        logger.info("ASSIGNED STUDENTS {} TO GROUP BY ID - {} SUCCESSFULLY", students.size(), groupId);
    }

    private void requiredStudentExistence(Integer studentId){
        if (!studentService.existsById(studentId)){
            throw new NotFoundException(format("Student by id- %d not exists", studentId));
        }
    }

    private void requiredGroupExistence(Integer groupId){
        if (!existsById(groupId)){
            throw new NotFoundException(format("Group by id - %d not exists", groupId));
        }
    }
}
