package ua.foxminded.task10.uml.service.impl;

import org.apache.log4j.Logger;
import ua.foxminded.task10.uml.dao.StudentDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class StudentServiceImpl implements StudentService {

    private static final Logger logger = Logger.getLogger(StudentServiceImpl.class);

    private final StudentDao studentDao;
    private final GroupService groupService;

    public StudentServiceImpl(StudentDao studentDao, GroupService groupService) {
        this.studentDao = studentDao;
        this.groupService = groupService;
    }

    @Override
    public Student save(Student student) {
        requireNonNull(student);
        getNotFoundException(student.getGroupId());
        logger.info(format("SAVING... %s", student));
        Student result = studentDao.save(student).orElseThrow(() -> new NotFoundException(format("Can't save %s", student)));
        logger.info(format("SAVED %s SUCCESSFULLY", result));
        return result;
    }

    @Override
    public Student findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... STUDENT BY ID - %d", id));
        Student result = studentDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find student by id - %d", id)));
        logger.info(format("FOUND %s BY ID - %d", result, id));
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... STUDENT EXISTS BY ID - %d", id));
        boolean result = studentDao.existsById(id);
        logger.info(format("STUDENT BY ID - %d EXISTS - %s", id, result));
        return result;
    }

    @Override
    public List<Student> findAll() {
        logger.info("FINDING... ALL STUDENTS");
        List<Student> result = studentDao.findAll();
        if (result.isEmpty()) {
            logger.info(format("FOUND %d STUDENTS", 0));
            return result;
        }
        logger.info(format("FOUND %d STUDENTS", result.size()));
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT STUDENTS");
        Long result = studentDao.count();
        logger.info(format("FOUND %d STUDENTS SUCCESSFULLY", result));
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING... STUDENT BY ID - %d", id));
        studentDao.deleteById(id);
        logger.info(format("DELETED STUDENT BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Student student) {
        requireNonNull(student);
        logger.info(format("DELETING... %s", student));
        studentDao.delete(student);
        logger.info(format("DELETED %s SUCCESSFULLY", student));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL STUDENTS");
        studentDao.deleteAll();
        logger.info("DELETED ALL STUDENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Student> students) {
        requireNonNull(students);
        for (Student student : students) {
            getNotFoundException(student.getGroupId());
        }
        logger.info(format("SAVING... %d STUDENTS", students.size()));
        studentDao.saveAll(students);
        logger.info(format("SAVED %d STUDENTS SUCCESSFULLY", students.size()));
    }

    @Override
    public List<Student> findByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        logger.info(format("FINDING... STUDENTS BY COURSE NUMBER - %d", courseNumber));
        List<Student> result = studentDao.findByCourseNumber(courseNumber);
        if (result.isEmpty()) {
            logger.info(format("FOUND %d STUDENTS BY COURSE NUMBER - %s", 0, courseNumber));
            return result;
        }
        logger.info(format("FOUND %d STUDENTS BY COURSE NUMBER - %d SUCCESSFULLY", result.size(), courseNumber));
        return result;
    }

    @Override
    public void updateStudent(Student student) {
        requireNonNull(student);
        getNotFoundException(student.getGroupId());
        logger.info(format("UPDATING STUDENT BY ID - %d", student.getId()));
        studentDao.updateStudent(student);
        logger.info(format("UPDATED STUDENT BY ID - %d SUCCESSFULLY", student.getId()));
    }

    @Override
    public List<Student> findStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        getNotFoundException(groupId);
        logger.info(format("FINDING... STUDENTS BY ID GROUP - %d", groupId));
        List<Student> result = studentDao.findStudentsByGroupId(groupId);
        if (result.isEmpty()) {
            logger.info(format("FOUND %d STUDENTS BY GROUP ID - %d", 0, groupId));
            return result;
        }
        logger.info(format("FOUND %d STUDENTS BY ID GROUP ID - %d", result.size(), groupId));
        return result;
    }

    private void getNotFoundException(Integer groupId) {
        if (!groupService.existsById(groupId)) {
            throw new NotFoundException(format("Group by id - %d not exists", groupId));
        }
    }
}
