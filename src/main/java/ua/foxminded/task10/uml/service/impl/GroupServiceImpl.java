package ua.foxminded.task10.uml.service.impl;

import org.apache.log4j.Logger;
import ua.foxminded.task10.uml.dao.GroupDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class GroupServiceImpl implements GroupService {

    private static final Logger logger = Logger.getLogger(GroupServiceImpl.class);

    private final GroupDao groupDao;
    private final StudentService studentService;

    public GroupServiceImpl(GroupDao groupDao, StudentService studentService) {
        this.groupDao = groupDao;
        this.studentService = studentService;
    }

    @Override
    public Group save(Group group) {
        requireNonNull(group);
        logger.info(format("SAVING... %s", group));
        Group result = groupDao.save(group).orElseThrow(() -> new NotFoundException(format("Can't save %s", group)));
        logger.info(format("SAVED %s SUCCESSFULLY", group));
        return result;
    }

    @Override
    public Group findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... GROUP BY ID - %d", id));
        Group result = groupDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find group by id- %d", id)));
        logger.info(format("FOUND %s BY ID - %d SUCCESSFULLY", result, id));
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... GROUP EXISTS BY ID - %d", id));
        boolean result = groupDao.existsById(id);
        logger.info(format("GROUP BY ID - %d EXISTS - %s", id, result));
        return result;
    }

    @Override
    public List<Group> findAll() {
        logger.info("FINDING... ALL GROUPS");
        List<Group> result = groupDao.findAll();
        if (result.isEmpty()){
            logger.info(format("FOUND %d GROUPS", 0));
            return result;
        }
        logger.info(format("FOUND %d GROUPS SUCCESSFULLY", result.size()));
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT GROUPS");
        Long result = groupDao.count();
        logger.info(format("FOUND COUNT(%d) GROUPS SUCCESSFULLY", result));
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING... GROUP BY ID- %d", id));
        groupDao.deleteById(id);
        logger.info(format("DELETED GROUP BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Group group) {
        requireNonNull(group);
        logger.info(format("DELETING... %s", group));
        groupDao.delete(group);
        logger.info(format("DELETED %s SUCCESSFULLY", group));
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
        logger.info(format("SAVING... %d GROUPS", groups.size()));
        groupDao.saveAll(groups);
        logger.info(format("SAVED %d GROUPS SUCCESSFULLY", groups.size()));
    }

    @Override
    public Group findByGroupName(String groupName) {
        requireNonNull(groupName);
        logger.info(format("FINDING... GROUP BY GROUP NAME - %s", groupName));
        Group result = groupDao.findByGroupName(groupName).orElseThrow(() -> new NotFoundException(format("Can't find group by group name - %s", groupName)));
        logger.info(format("FOUND %s BY GROUP NAME - %s SUCCESSFULLY", result, groupName));
        return result;
    }

    @Override
    public void updateGroup(Group group) {
        requireNonNull(group);
        logger.info(format("UPDATING... %s", group));
        groupDao.updateGroup(group);
        logger.info(format("UPDATED %s SUCCESSFULLY", group));
    }

    @Override
    public void assignStudentToGroup(Integer studentId, Integer groupId) {
        requireNonNull(studentId);
        requireNonNull(groupId);
        getNotFoundException(studentId, groupId);
        logger.info(format("ASSIGNING STUDENT BY ID - %d TO GROUP BY ID- %d", studentId, groupId));
        groupDao.assignStudentToGroup(studentId, groupId);
        logger.info(format("ASSIGNED STUDENT BY ID - %d TO GROUP BY ID - %d SUCCESSFULLY", studentId, groupId));
    }

    @Override
    public void assignStudentsToGroup(List<Student> students, Integer groupId) {
        requireNonNull(students);
        requireNonNull(groupId);
        for (Student student: students){
            getNotFoundException(student.getId(), groupId);
        }
        logger.info(format("ASSIGNING STUDENTS %d TO GROUP BY ID - %d", students.size(), groupId));
        groupDao.assignStudentsToGroup(students, groupId);
        logger.info(format("ASSIGNED STUDENTS %d TO GROUP BY ID - %d SUCCESSFULLY", students.size(), groupId));
    }

    private void getNotFoundException(Integer studentId, Integer groupId){
        if (!studentService.existsById(studentId) || !this.existsById(groupId)){
            throw new NotFoundException(format("Student by id - %d or Group by id - %d not exists", studentId, groupId));
        }
    }
}
