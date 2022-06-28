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
import ua.foxminded.task10.uml.service.StudentService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentServiceImpl implements StudentService {

    StudentRepository studentRepository;
    GroupRepository groupRepository;

    @Override
    public Student save(Student student) {
        requireNonNull(student);
        log.info("SAVING... {}", student);
        Student result = studentRepository.save(student);
        log.info("SAVED {} SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Student findById(Integer studentId) {
        requireNonNull(studentId);
        requiredStudentExistence(studentId);
        log.info("FINDING... STUDENT BY ID - {}", studentId);
        Student result = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException(format("Can't find student by studentId - %d", studentId)));
        log.info("FOUND {} BY ID - {}", result, studentId);
        return result;
    }

    @Override
    public List<Student> findStudentsByNameOrSurname(Student student) {
        requireNonNull(student);
        log.info("FINDING... STUDENTS BY NAME OR SURNAME");
        List<Student> result = studentRepository.findStudentsByNameOrSurname(student);
        log.info("FOUND STUDENTS {} BY NAME OR SURNAME", result.size());
        return result;
    }

    @Override
    public boolean existsById(Integer studentId) {
        requireNonNull(studentId);
        log.info("CHECKING... STUDENT EXISTS BY ID - {}", studentId);
        boolean result = studentRepository.existsById(studentId);
        log.info("STUDENT BY ID - {} EXISTS - {}", studentId, result);
        return result;
    }

    @Override
    public List<Student> findAll() {
        log.info("FINDING... ALL STUDENTS");
        List<Student> result = studentRepository.findAll();
        log.info("FOUND {} STUDENTS", result.size());
        return result;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT STUDENTS");
        Long result = studentRepository.count();
        log.info("FOUND {} STUDENTS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Long countByGroupId(Integer groupId) {
        log.info("FINDING... COUNT BY GROUP ID - {}", groupId);
        Long result = studentRepository.countByGroupId(groupId);
        log.info("FOUND {} COUNT BY GROUP ID - {}", result, groupId);
        return result;
    }

    @Override
    public void deleteById(Integer studentId) {
        requireNonNull(studentId);
        requiredStudentExistence(studentId);
        log.info("DELETING... STUDENT BY ID - {}", studentId);
        studentRepository.deleteById(studentId);
        log.info("DELETED STUDENT BY ID - {} SUCCESSFULLY", studentId);
    }

    @Override
    public void deleteStudentsByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        log.info("DELETING... STUDENTS BY COURSE NUMBER - {}", courseNumber);
        studentRepository.deleteStudentsByCourse(courseNumber);
        log.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", courseNumber);
    }

    @Override
    public void deleteStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("DELETING... STUDENTS BY GROUP ID {}", groupId);
        studentRepository.deleteStudentsByGroupId(groupId);
        log.info("DELETED STUDENTS BY GROUP ID {}", groupId);
    }

    @Override
    public void delete(Student student) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL STUDENTS");
        studentRepository.deleteAll();
        log.info("DELETED ALL STUDENTS SUCCESSFULLY");
    }

    @Override
    public List<Student> findStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("FINDING... STUDENTS BY GROUP ID - {}", groupId);
        List<Student> students = studentRepository.findStudentsByGroupIdOrderByFirstName(groupId);
        log.info("FOUND {} STUDENTS BY GROUP ID - {} SUCCESSFULLY", students.size(), groupId);
        return students;
    }

    @Override
    public void saveAll(List<Student> students) {
        requireNonNull(students);
        log.info("SAVING... {} STUDENTS", students.size());
        studentRepository.saveAll(students);
        log.info("SAVED {} STUDENTS SUCCESSFULLY", students.size());
    }

    @Override
    public List<Student> findByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        log.info("FINDING... STUDENTS BY COURSE NUMBER - {}", courseNumber);
        List<Student> result = studentRepository.findStudentsByCourseOrderByFirstName(courseNumber);
        log.info("FOUND {} STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", result.size(), courseNumber);
        return result;
    }

    @Override
    public void updateStudent(Integer studentId, Student updatedStudent) {
        requireNonNull(updatedStudent);
        requireNonNull(studentId);
        requiredStudentExistence(studentId);
        log.info("UPDATING STUDENT BY ID - {}", studentId);
        updatedStudent.setId(studentId);
        studentRepository.save(updatedStudent);
        log.info("UPDATED STUDENT BY ID - {} SUCCESSFULLY", studentId);
    }

    @Override
    public void updateTheStudentGroup(Integer groupId, Integer studentId) {
        requireNonNull(groupId);
        requireNonNull(studentId);
        requiredGroupExistence(groupId);
        requiredStudentExistence(studentId);
        log.info("UPDATING... THE STUDENTS' BY ID - {} GROUP BY ID - {}", studentId, groupId);
        studentRepository.updateTheStudentGroup(groupId, studentId);
        log.info("UPDATED THE STUDENTS' BY ID - {} GROUP BY ID - {}", studentId, groupId);
    }

    @Override
    public void deleteTheStudentGroup(Integer studentId) {
        requireNonNull(studentId);
        requiredStudentExistence(studentId);
        log.info("UPDATING... STUDENTS' BY ID - {} GROUP", studentId);
        studentRepository.deleteTheStudentGroup(studentId);
        log.info("UPDATED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", studentId);
    }

    @Override
    public List<Student> findStudentsByGroupName(Group group) {
        requireNonNull(group.getName());
        requiredGroupExistence(group.getId());
        log.info("FINDING... STUDENTS BY NAME GROUP - {}", group.getName());
        List<Student> result = studentRepository.findStudentsByGroupName(group);
        log.info("FOUND {} STUDENTS BY ID GROUP NAME - {}", result.size(), group.getName());
        return result;
    }

    private void requiredGroupExistence(Integer groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(format("Group by id - %s not exists", groupId));
        }
    }

    private void requiredStudentExistence(Integer studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException(format("Student by id- %d not exists", studentId));
        }
    }
}
