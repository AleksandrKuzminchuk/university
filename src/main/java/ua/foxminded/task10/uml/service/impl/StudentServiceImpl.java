package ua.foxminded.task10.uml.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.mapper.GroupMapper;
import ua.foxminded.task10.uml.dto.mapper.StudentMapper;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.repository.StudentRepository;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final GroupService groupService;
    private final StudentMapper studentMapper;
    private final GroupMapper groupMapper;

    public StudentServiceImpl(StudentRepository studentRepository, @Lazy GroupService groupService, StudentMapper studentMapper, GroupMapper groupMapper) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
        this.studentMapper = studentMapper;
        this.groupMapper = groupMapper;
    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        log.info("SAVING... {}", studentDTO);
        Student student = studentMapper.convertToStudent(studentDTO);
        Student saveStudent = studentRepository.save(student);
        StudentDTO savedStudentDTO = studentMapper.convertToStudentDTO(saveStudent);
        log.info("SAVED {} SUCCESSFULLY", savedStudentDTO);
        return savedStudentDTO;
    }

    @Override
    public StudentDTO findById(Integer studentId) {
        requireNonNull(studentId);
        requiredStudentExistence(studentId);
        log.info("FINDING... STUDENT BY ID - {}", studentId);
        Student result = studentRepository.findById(studentId).orElseThrow(() -> new GlobalNotFoundException(format("Can't find student by studentId - %d", studentId)));
        StudentDTO studentDTO = studentMapper.convertToStudentDTO(result);
        log.info("FOUND {} BY ID - {}", studentDTO, studentId);
        return studentDTO;
    }

    @Override
    public List<StudentDTO> findByNameOrSurname(String firstName, String lastName) {
        log.info("FINDING... STUDENTS BY NAME OR SURNAME");
        List<Student> result = studentRepository.findByFirstNameOrLastNameOrderByFirstName(firstName, lastName);
        List<StudentDTO> studentsDTO = getStudentsDTO(result);
        log.info("FOUND STUDENTS {} BY NAME OR SURNAME", studentsDTO.size());
        return studentsDTO;
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
    public List<StudentDTO> findAll() {
        log.info("FINDING... ALL STUDENTS");
        List<Student> result = studentRepository.findAll(Sort.by(Sort.Order.asc("firstName")));
        List<StudentDTO> studentsDTO = getStudentsDTO(result);
        log.info("FOUND {} STUDENTS", studentsDTO.size());
        return studentsDTO;
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
        requireNonNull(groupId);
        log.info("FINDING... COUNT BY GROUP ID - {}", groupId);
        Long result = studentRepository.countByGroupId(groupId);
        log.info("FOUND {} COUNT BY GROUP ID - {}", result, groupId);
        return result;
    }

    @Override
    public Long countByCourse(Integer course) {
        requireNonNull(course);
        log.info("FINDING... COUNT BY COURSE - {}", course);
        Long result = studentRepository.countByCourse(course);
        log.info("FOUND {} COUNT BY COURSE - {}", result, course);
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
    public void deleteByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        log.info("DELETING... STUDENTS BY COURSE NUMBER - {}", courseNumber);
        studentRepository.deleteByCourse(courseNumber);
        log.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", courseNumber);
    }

    @Override
    public void deleteByGroupId(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("DELETING... STUDENTS BY GROUP ID {}", groupId);
        studentRepository.deleteByGroupId(groupId);
        log.info("DELETED STUDENTS BY GROUP ID {}", groupId);
    }

    @Override
    public void delete(StudentDTO studentDTO) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL STUDENTS");
        studentRepository.deleteAll();
        log.info("DELETED ALL STUDENTS SUCCESSFULLY");
    }

    @Override
    public List<StudentDTO> findByGroupId(Integer groupId) {
        requireNonNull(groupId);
        requiredGroupExistence(groupId);
        log.info("FINDING... STUDENTS BY GROUP ID - {}", groupId);
        List<Student> students = studentRepository.findByGroupIdOrderByFirstName(groupId);
        List<StudentDTO> studentsDTO = getStudentsDTO(students);
        log.info("FOUND {} STUDENTS BY GROUP ID - {} SUCCESSFULLY", studentsDTO.size(), groupId);
        return studentsDTO;
    }

    @Override
    public Integer saveAll(List<StudentDTO> studentsDTO) {
        requireNonNull(studentsDTO);
        log.info("SAVING... {} STUDENTS", studentsDTO.size());
        List<Student> students = studentsDTO.stream().map(studentMapper::convertToStudent).collect(Collectors.toList());
        studentRepository.saveAll(students);
        log.info("SAVED {} STUDENTS SUCCESSFULLY", students.size());
        return students.size();
    }

    @Override
    public List<StudentDTO> findByCourseNumber(Integer courseNumber) {
        log.info("FINDING... STUDENTS BY COURSE NUMBER - {}", courseNumber);
        List<Student> result = studentRepository.findByCourseOrderByFirstName(courseNumber);
        List<StudentDTO> studentsDTO = getStudentsDTO(result);
        log.info("FOUND {} STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", studentsDTO.size(), courseNumber);
        return studentsDTO;
    }

    public StudentDTO update(StudentDTO studentDTO) {
        log.info("UPDATING STUDENT - {}", studentDTO);
        requiredStudentExistence(studentDTO.getId());
        requiredGroupExistence(studentDTO.getGroup().getId());
        GroupDTO groupDTO = groupService.findById(studentDTO.getGroup().getId());
        Student student = studentMapper.convertToStudent(studentDTO);
        Group group = groupMapper.convertToGroup(groupDTO);
        student.setGroup(group);
        Student updatedStudent = studentRepository.save(student);
        StudentDTO updatedStudentDTO = studentMapper.convertToStudentDTO(updatedStudent);
        log.info("UPDATED SUCCESSFULLY");
        return updatedStudentDTO;
    }

    @Override
    public StudentDTO deleteGroup(Integer studentId) {
        requiredStudentExistence(studentId);
        log.info("UPDATING... STUDENTS' BY ID - {} GROUP", studentId);
        StudentDTO studentDTO = findById(studentId);
        Student student = studentMapper.convertToStudent(studentDTO);
        student.setGroup(null);
        Student savedStudent = studentRepository.save(student);
        StudentDTO savedStudentDTO = studentMapper.convertToStudentDTO(savedStudent);
        log.info("UPDATED THE STUDENTS' BY ID - {} GROUP SUCCESSFULLY", studentId);
        return savedStudentDTO;
    }

    @Override
    public List<StudentDTO> findByGroupName(String groupName) {
        log.info("FINDING... STUDENTS BY NAME GROUP - {}", groupName);
        List<Student> result = studentRepository.findAllByGroup_Name(groupName, Sort.by(Sort.Order.asc("firstName")));
        List<StudentDTO> studentsDTO = getStudentsDTO(result);
        log.info("FOUND {} STUDENTS BY ID GROUP NAME - {}", studentsDTO.size(), groupName);
        return studentsDTO;
    }

    private List<StudentDTO> getStudentsDTO(List<Student> students) {
        return students.stream().map(studentMapper::convertToStudentDTO).collect(Collectors.toList());
    }

    private void requiredGroupExistence(Integer groupId) {
        if (!groupService.existsById(groupId)) {
            throw new GlobalNotFoundException(format("Group by id - %s not exists", groupId));
        }
    }

    private void requiredStudentExistence(Integer studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new GlobalNotFoundException(format("Student by id- %d not exists", studentId));
        }
    }

    private void requireNonNull(Object o) {
        if (o == null) {
            throw new GlobalNotNullException("Can't be null " + o.getClass().getName());
        }
    }
}
