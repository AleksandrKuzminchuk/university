package ua.foxminded.task10.uml.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.mapper.SubjectMapper;
import ua.foxminded.task10.uml.dto.mapper.TeacherMapper;
import ua.foxminded.task10.uml.dto.response.TeacherAddSubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherFindSubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherUpdateSubjectResponse;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.repository.SubjectRepository;
import ua.foxminded.task10.uml.repository.TeacherRepository;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotFoundException;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotNullException;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotValidException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final SubjectService subjectService;
    private final TeacherMapper teacherMapper;
    private final SubjectMapper subjectMapper;
    private final SubjectRepository subjectRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, @Lazy SubjectService subjectService
            , SubjectRepository subjectRepository, TeacherMapper teacherMapper, SubjectMapper subjectMapper) {
        this.teacherRepository = teacherRepository;
        this.subjectService = subjectService;
        this.subjectRepository = subjectRepository;
        this.teacherMapper = teacherMapper;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public TeacherDTO save(TeacherDTO teacherDTO) {
        log.info("SAVING... {}", teacherDTO);
        Teacher teacher = teacherMapper.map(teacherDTO);
        Teacher savedTeacher = teacherRepository.save(teacher);
        TeacherDTO savedTeacherDTO = teacherMapper.map(savedTeacher);
        log.info("SAVED {} SUCCESSFULLY", savedTeacherDTO);
        return savedTeacherDTO;
    }

    @Override
    public TeacherDTO findById(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("FINDING... TEACHER BY ID - {}", teacherId);
        Teacher result = extractTeacherByIdWithRepo(teacherId);
        TeacherDTO teacherDTO = teacherMapper.map(result);
        log.info("FOUND {} BY ID - {}", teacherDTO, teacherId);
        return teacherDTO;
    }

    @Override
    public boolean existsById(Integer teacherId) {
        requireNonNull(teacherId);
        log.info("CHECKING... TEACHER EXISTS BY ID - {}", teacherId);
        boolean result = teacherRepository.existsById(teacherId);
        log.info("TEACHER BY ID - {} EXISTS - {}", teacherId, result);
        return result;
    }

    @Override
    public List<TeacherDTO> findAll() {
        log.info("FINDING... ALL TEACHERS");
        List<Teacher> result = teacherRepository.findAll(Sort.by(Sort.Order.asc("firstName")));
        List<TeacherDTO> teachersDTO = getTeachersDTO(result);
        log.info("FOUND {} TEACHERS", teachersDTO.size());
        return teachersDTO;
    }

    @Override
    public List<TeacherDTO> findByNameOrSurname(String name, String surname) {
        requireNonNull(name);
        requireNonNull(surname);
        log.info("FINDING... TEACHERS {} {}", name, surname);
        List<Teacher> result = teacherRepository.findByFirstNameOrLastName(name, surname, Sort.by(Sort.Order.asc("firstName")));
        List<TeacherDTO> teachersDTO = getTeachersDTO(result);
        log.info("FOUND {} TEACHERS BY {} {} SUCCESSFULLY", teachersDTO.size(), name, surname);
        return teachersDTO;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT TEACHERS");
        Long result = teacherRepository.count();
        log.info("FOUND {} TEACHERS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public void deleteById(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("DELETING... TEACHER BY ID - {}", teacherId);
        teacherRepository.deleteById(teacherId);
        log.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
    }

    @Override
    public void delete(TeacherDTO teacherDTO) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL TEACHERS");
        teacherRepository.deleteAll();
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
    }

    @Override
    public void deleteSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(subjectId);
        if (!teacherRepository.existsSubjectAndTeacher(teacherId, subjectId)) {
            throw new GlobalNotFoundException(format("Can't delete because the teacher hasn't the subject of id [%d]", subjectId));
        }
        log.info("DELETING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        Teacher teacher = extractTeacherByIdWithRepo(teacherId);
        Subject subjectToRemove = extractSubjectByIdWithRepo(subjectId);
        teacher.getSubjects().remove(subjectToRemove);
        subjectToRemove.getTeachers().remove(teacher);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} SUCCESSFULLY", teacherId, subjectId);
    }

    @Override
    public void saveAll(List<TeacherDTO> teachersDTO) {
        requireNonNull(teachersDTO);
        log.info("SAVING... {} TEACHERS", teachersDTO.size());
        List<Teacher> teachers = teachersDTO.stream().map(teacherMapper::map).collect(Collectors.toList());
        teacherRepository.saveAll(teachers);
        log.info("SAVED {} TEACHERS SUCCESSFULLY", teachers.size());
    }

    @Override
    public void update(TeacherDTO teacherDTO) {
        requiredTeacherExistence(teacherDTO.getId());
        log.info("UPDATING... TEACHER BY ID - {}", teacherDTO.getId());
        Teacher updatedTeacher = teacherMapper.map(teacherDTO);
        Teacher savedTeacher = teacherRepository.save(updatedTeacher);
        teacherMapper.map(savedTeacher);
        log.info("UPDATED TEACHER BY ID - {} SUCCESSFULLY", savedTeacher.getId());
    }

    @Override
    public TeacherUpdateSubjectResponse updateSubjectForm(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(subjectId);
        log.info("PREPARING FORM UPDATE SUBJECT ID - {} BY TEACHER ID - {}", subjectId, teacherId);
        TeacherDTO teacherDTO = this.findById(teacherId);
        SubjectDTO subjectDTO = subjectService.findById(subjectId);
        List<SubjectDTO> subjectsDTO = subjectService.findAll();
        TeacherUpdateSubjectResponse response = new TeacherUpdateSubjectResponse(teacherDTO, subjectDTO, subjectsDTO);
        log.info("PREPARED FORM UPDATE SUBJECT ID - {} BY TEACHER ID - {}", subjectId, teacherId);
        return response;
    }

    @Override
    public SubjectDTO updateSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId) {
        requireNonNull(teacherId);
        requireNonNull(oldSubjectId);
        requireNonNull(newSubjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(oldSubjectId);
        requiredSubjectExistence(newSubjectId);
        checkToUniqueSubjectInList(teacherId, newSubjectId);
        log.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubjectId);
        Teacher teacher = extractTeacherByIdWithRepo(teacherId);
        Subject oldSubject = extractSubjectByIdWithRepo(oldSubjectId);
        Subject newSubject = extractSubjectByIdWithRepo(newSubjectId);
        teacher.getSubjects().remove(oldSubject);
        teacher.getSubjects().add(newSubject);
        SubjectDTO subjectDTOToBeUpdated = getSubjectDTO(newSubject);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, oldSubjectId, newSubjectId);
        return subjectDTOToBeUpdated;
    }

    @Override
    public TeacherAddSubjectResponse addSubjectFrom(Integer id) {
        requireNonNull(id);
        requiredTeacherExistence(id);
        log.info("PREPARING FORM ADD SUBJECT TO TEACHER BY ID- {}", id);
        TeacherDTO teacherDTO = this.findById(id);
        List<SubjectDTO> subjectsDTO = subjectService.findAll();
        TeacherAddSubjectResponse response = new TeacherAddSubjectResponse(teacherDTO, subjectsDTO);
        log.info("PREPARED FORM ADD SUBJECT TO TEACHER BY ID- {}", id);
        return response;
    }

    @Override
    public SubjectDTO addSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(subjectId);
        checkToUniqueSubjectInList(teacherId, subjectId);
        log.info("ADDING... TEACHER BY ID - {} TO SUBJECT BY ID - {}", teacherId, subjectId);
        Teacher teacherToBeSave = extractTeacherByIdWithRepo(teacherId);
        Subject subjectToBeSave = extractSubjectByIdWithRepo(subjectId);
        teacherToBeSave.getSubjects().add(subjectToBeSave);
        SubjectDTO subjectDTOToBeAdded = getSubjectDTO(subjectToBeSave);
        log.info("ADDED TEACHER BT ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, subjectId);
        return subjectDTOToBeAdded;
    }

    @Override
    public List<SubjectDTO> addSubjects(TeacherDTO teacherDTO, List<SubjectDTO> subjectsDTO) {
        requireNonNull(teacherDTO);
        requireNonNull(subjectsDTO);
        requiredTeacherExistence(teacherDTO.getId());
        subjectsDTO.forEach(this::requiredSubjectExistence);
        log.info("ADDING... TEACHER BY ID - {} TO SUBJECTS {}", teacherDTO.getId(), subjectsDTO.size());
        subjectsDTO.forEach(subject -> addSubject(teacherDTO.getId(), subject.getId()));
        log.info("ADDED TEACHER BY ID - {} TO SUBJECTS {} SUCCESSFULLY", teacherDTO.getId(), subjectsDTO.size());
        return subjectsDTO;
    }

    @Override
    public TeacherFindSubjectResponse findSubjectsForm(Integer id) {
        requireNonNull(id);
        requiredTeacherExistence(id);
        log.info("PREPARING FORM FIND SUBJECTS BY TEACHER ID - {}", id);
        TeacherDTO teacherDTO = this.findById(id);
        List<SubjectDTO> subjectsDTO = this.findSubjects(id);
        TeacherFindSubjectResponse response = new TeacherFindSubjectResponse(teacherDTO, subjectsDTO);
        log.info("PREPARED FORM FIND SUBJECTS BY TEACHER ID - {} SUCCESSFULLY", id);
        return response;
    }

    @Override
    public List<SubjectDTO> findSubjects(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("FINDING... SUBJECTS BY TEACHER ID - {}", teacherId);
        Teacher teacher = extractTeacherByIdWithRepo(teacherId);
        List<Subject> subjects = teacher.getSubjects();
        List<SubjectDTO> subjectsDTO = subjects.stream().map(subjectMapper::map).collect(Collectors.toList());
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", teacher.getSubjects().size(), teacherId);
        return subjectsDTO;
    }

    private SubjectDTO getSubjectDTO(Subject newSubject) {
        return subjectMapper.map(newSubject);
    }

    private void checkToUniqueSubjectInList(Integer teacherId, Integer subjectId) {
        this.findSubjects(teacherId).forEach(subjectFromList -> {
            if (subjectFromList.getId().equals(subjectId)) {
                throw new GlobalNotValidException(format("The teacher already has the subject by id - [%d]", subjectId));
            }});
    }

    private Subject extractSubjectByIdWithRepo(Integer subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find subject by subjectId - %d", subjectId)));
    }

    private Teacher extractTeacherByIdWithRepo(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find teacher by teacherId - %d", teacherId)));
    }

    private List<TeacherDTO> getTeachersDTO(List<Teacher> teachers) {
        return teachers.stream().map(teacherMapper::map).collect(Collectors.toList());
    }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!teacherRepository.existsById(teacherId))
            throw new GlobalNotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

    private void requiredSubjectExistence(SubjectDTO subjectDTO) {
        if (!subjectService.existsById(subjectDTO.getId()))
            throw new GlobalNotFoundException(format("Subject by id - %d not exists", subjectDTO.getId()));
    }

    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectService.existsById(subjectId))
            throw new GlobalNotFoundException(format("Subject by id - %d not exists", subjectId));
    }

    private void requireNonNull(Object o) {
        if (o == null) {
            throw new GlobalNotNullException("Can't be null " + o.getClass().getName());
        }
    }
}
