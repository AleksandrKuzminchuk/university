package ua.foxminded.task10.uml.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.GroupDao;
import ua.foxminded.task10.uml.dao.StudentDao;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Component
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentDao studentDao;
    private final GroupDao groupDao;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao, GroupDao groupDao) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }

    @Override
    public Student save(Student student) {
        requireNonNull(student);
        logger.info("SAVING... {}", student);
        Student result = studentDao.save(student).orElseThrow(() -> new NotFoundException(format("Can't save %s", student)));
        logger.info("SAVED {} SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Student findById(Integer id) {
        requireNonNull(id);
        requiredStudentExistence(id);
        logger.info("FINDING... STUDENT BY ID - {}", id);
        Student result = studentDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find student by id - %d", id)));
        logger.info("FOUND {} BY ID - {}", result, id);
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... STUDENT EXISTS BY ID - {}", id);
        boolean result = studentDao.existsById(id);
        logger.info("STUDENT BY ID - {} EXISTS - {}", id, result);
        return result;
    }

    @Override
    public List<Student> findAll() {
        logger.info("FINDING... ALL STUDENTS");
        List<Student> result = studentDao.findAll();
        logger.info("FOUND {} STUDENTS", result.size());
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT STUDENTS");
        Long result = studentDao.count();
        logger.info("FOUND {} STUDENTS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        requiredStudentExistence(id);
        logger.info("DELETING... STUDENT BY ID - {}", id);
        studentDao.deleteById(id);
        logger.info("DELETED STUDENT BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Student student) {
        requireNonNull(student);
        requiredStudentExistence(student.getId());
        logger.info("DELETING... {}", student);
        studentDao.delete(student);
        logger.info("DELETED {} SUCCESSFULLY", student);
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
        logger.info("SAVING... {} STUDENTS", students.size());
        studentDao.saveAll(students);
        logger.info("SAVED {} STUDENTS SUCCESSFULLY", students.size());
    }

    @Override
    public List<Student> findByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        logger.info("FINDING... STUDENTS BY COURSE NUMBER - {}", courseNumber);
        List<Student> result = studentDao.findByCourseNumber(courseNumber);
        logger.info("FOUND {} STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", result.size(), courseNumber);
        return result;
    }

    @Override
    public void updateStudent(Student student) {
        requireNonNull(student);
        requiredStudentExistence(student.getId());
        logger.info("UPDATING STUDENT BY ID - {}", student.getId());
        studentDao.updateStudent(student);
        logger.info("UPDATED STUDENT BY ID - {} SUCCESSFULLY", student.getId());
    }

    @Override
    public List<Student> findStudentsByGroupName(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        logger.info("FINDING... STUDENTS BY ID GROUP - {}", groupId);
        List<Student> result = studentDao.findStudentsByGroupName(groupId);
        logger.info("FOUND {} STUDENTS BY ID GROUP ID - {}", result.size(), groupId);
        return result;
    }

    private void requiredGroupExistence(Integer groupId) {
        if (!groupDao.existsById(groupId)) {
            throw new NotFoundException(format("Group by id - %d not exists", groupId));
        }
    }

    private void requiredStudentExistence(Integer studentId) {
        if (!studentDao.existsById(studentId)) {
            throw new NotFoundException(format("Student by id- %d not exists", studentId));
        }
    }
}
