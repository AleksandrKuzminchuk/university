package ua.foxminded.task10.uml.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.mapper.SubjectMapper;
import ua.foxminded.task10.uml.dto.mapper.TeacherMapper;
import ua.foxminded.task10.uml.dto.response.SubjectAddTeacherResponse;
import ua.foxminded.task10.uml.dto.response.SubjectFindTeachersResponse;
import ua.foxminded.task10.uml.dto.response.SubjectUpdateTeacherResponse;
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
public class SubjectServiceImpl implements SubjectService {


    private final TeacherService teacherService;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectMapper subjectMapper;
    private final TeacherMapper teacherMapper;

    @Autowired
    public SubjectServiceImpl(@Lazy TeacherService teacherService, SubjectRepository subjectRepository, TeacherRepository teacherRepository,
                              SubjectMapper subjectMapper, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public SubjectDTO save(SubjectDTO subjectDTO) {
        log.info("SAVING... {}", subjectDTO);
        Subject subject = subjectMapper.convertToSubject(subjectDTO);
        Subject savedSubject = subjectRepository.save(subject);
        SubjectDTO savedSubjectDTO = subjectMapper.convertToSubjectDTO(savedSubject);
        log.info("SAVED {} SUCCESSFULLY", savedSubjectDTO);
        return savedSubjectDTO;
    }

    @Override
    public SubjectDTO findById(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("FINDING... SUBJECT BY ID - {}", subjectId);
        Subject result = extractSubjectByIdWithRepo(subjectId);
        SubjectDTO subjectDTO = subjectMapper.convertToSubjectDTO(result);
        log.info("FOUND {} BY ID - {} SUCCESSFULLY", subjectDTO, subjectId);
        return subjectDTO;
    }

    @Override
    public SubjectDTO findByName(String subjectName) {
        log.info("FINDING... SUBJECT BY NAME {}", subjectName);
        Subject result = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find subjectName by name - %s", subjectName)));
        SubjectDTO subjectDTO = subjectMapper.convertToSubjectDTO(result);
        log.info("FOUND {} SUBJECT BY NAME {}", subjectDTO, subjectName);
        return subjectDTO;
    }

    @Override
    public boolean existsById(Integer subjectId) {
        requireNonNull(subjectId);
        log.info("CHECKING... SUBJECT EXISTS BY ID - {}", subjectId);
        boolean result = subjectRepository.existsById(subjectId);
        log.info("SUBJECT BY ID - {} EXISTS - {}", subjectId, result);
        return result;
    }

    @Override
    public List<SubjectDTO> findAll() {
        log.info("FINDING... ALL SUBJECTS");
        List<Subject> result = subjectRepository.findAll();
        List<SubjectDTO> subjectsDTO = result.stream().map(subjectMapper::convertToSubjectDTO).collect(Collectors.toList());
        log.info("FOUND {} SUBJECTS SUCCESSFULLY", subjectsDTO.size());
        return subjectsDTO;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT SUBJECTS");
        Long result = subjectRepository.count();
        log.info("FOUND {} SUBJECTS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public void deleteById(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("DELETING SUBJECT BY ID - {}", subjectId);
        subjectRepository.deleteById(subjectId);
        log.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", subjectId);
    }

    @Override
    public void delete(SubjectDTO subjectDTO) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL SUBJECTS");
        subjectRepository.deleteAll();
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void deleteTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(teacherId);
        if (!teacherRepository.existsSubjectAndTeacher(teacherId, subjectId)) {
            throw new GlobalNotFoundException(format("Can't delete because the subject hasn't the teacher of id [%d]", teacherId));
        }
        log.info("DELETING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        Subject subject = extractSubjectByIdWithRepo(subjectId);
        Teacher teacherToRemove = extractTeacherByIdWithRepo(teacherId);
        subject.getTeachers().remove(teacherToRemove);
        teacherToRemove.getSubjects().remove(subject);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
    }

    @Override
    public void saveAll(List<SubjectDTO> subjectsDTO) {
        requireNonNull(subjectsDTO);
        log.info("SAVING {} SUBJECTS", subjectsDTO.size());
        List<Subject> subjects = subjectsDTO.stream().map(subjectMapper::convertToSubject).collect(Collectors.toList());
        subjectRepository.saveAll(subjects);
        log.info("SAVED {} SUBJECTS SUCCESSFULLY", subjects.size());
    }

    @Override
    public void update(SubjectDTO subjectDTO) {
        requiredSubjectExistence(subjectDTO.getId());
        log.info("UPDATING... SUBJECT BY ID - {}", subjectDTO.getId());
        Subject subject = subjectMapper.convertToSubject(subjectDTO);
        Subject updatedSubject = subjectRepository.save(subject);
        subjectMapper.convertToSubjectDTO(updatedSubject);
        log.info("UPDATED {} SUCCESSFULLY", updatedSubject);
    }

    @Override
    public SubjectUpdateTeacherResponse updateTeacherForm(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(subjectId);
        log.info("PREPARING FORM UPDATE TEACHER ID - {} BY SUBJECT ID - {}", teacherId, subjectId);
        SubjectDTO subjectDTO = this.findById(subjectId);
        TeacherDTO teacherDTO = teacherService.findById(teacherId);
        List<TeacherDTO> teachersDTO = teacherService.findAll();
        SubjectUpdateTeacherResponse response = new SubjectUpdateTeacherResponse(subjectDTO, teacherDTO, teachersDTO);
        log.info("PREPARED FORM UPDATE TEACHER ID - {} BY SUBJECT ID - {} SUCCESSFULLY", teacherId, subjectId);
        return response;
    }

    @Override
    public TeacherDTO updateTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId) {
        requireNonNull(subjectId);
        requireNonNull(oldTeacherId);
        requireNonNull(newTeacherId);
        requiredTeacherExistence(oldTeacherId);
        requiredTeacherExistence(newTeacherId);
        checkToUniqueTeacherInList(subjectId, newTeacherId);
        log.info("UPDATING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
        Subject subject = extractSubjectByIdWithRepo(subjectId);
        Teacher oldTeacher = extractTeacherByIdWithRepo(oldTeacherId);
        Teacher newTeacher = extractTeacherByIdWithRepo(newTeacherId);
        subject.getTeachers().remove(oldTeacher);
        subject.getTeachers().add(newTeacher);
        TeacherDTO teacherDTOToBeUpdated = getTeacherDTO(newTeacher);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
        return teacherDTOToBeUpdated;
    }

    @Override
    public SubjectFindTeachersResponse findTeachersForm(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("PREPARING FORM FIND TEACHERS BY SUBJECT ID - {}", subjectId);
        SubjectDTO subjectDTO = this.findById(subjectId);
        List<TeacherDTO> teachersDTO = this.findTeachers(subjectId);
        SubjectFindTeachersResponse response = new SubjectFindTeachersResponse(subjectDTO, teachersDTO);
        log.info("PREPARED FORM FIND TEACHERS BY SUBJECT ID - {}", subjectId);
        return response;
    }

    @Override
    public List<TeacherDTO> findTeachers(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("FINDING... TEACHERS BY SUBJECT ID - {}", subjectId);
        Subject subject = extractSubjectByIdWithRepo(subjectId);
        List<Teacher> teachers = subject.getTeachers();
        List<TeacherDTO> teachersDTO = teachers.stream().map(teacherMapper::convertToTeacherDTO).collect(Collectors.toList());
        log.info("FOUND {} TEACHERS BY TEACHER ID - {}", subject.getTeachers().size(), subjectId);
        return teachersDTO;
    }

    @Override
    public SubjectAddTeacherResponse addTeacherForm(Integer id) {
        requireNonNull(id);
        requiredSubjectExistence(id);
        log.info("PREPARING FORM ADD TEACHER TO SUBJECT BY ID- {}", id);
        SubjectDTO subjectDTO = this.findById(id);
        List<TeacherDTO> teachersDTO = teacherService.findAll();
        SubjectAddTeacherResponse response = new SubjectAddTeacherResponse(subjectDTO, teachersDTO);
        log.info("PREPARED FORM ADD TEACHER TO SUBJECT BY ID - {}", id);
        return response;
    }

    @Override
    public TeacherDTO addTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        checkToUniqueTeacherInList(subjectId, teacherId);
        log.info("ADDING... SUBJECT BY ID - {} TO TEACHER BY ID - {}", subjectId, teacherId);
        Teacher teacherToBeAdd = extractTeacherByIdWithRepo(teacherId);
        Subject subjectToBeSave = extractSubjectByIdWithRepo(subjectId);
        subjectToBeSave.getTeachers().add(teacherToBeAdd);
        TeacherDTO teacherDTOToBeAdd = getTeacherDTO(teacherToBeAdd);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
        return teacherDTOToBeAdd;
    }

    @Override
    public List<TeacherDTO> addTeachers(SubjectDTO subjectDTO, List<TeacherDTO> teachersDTO) {
        requireNonNull(subjectDTO);
        requireNonNull(teachersDTO);
        requiredSubjectExistence(subjectDTO.getId());
        teachersDTO.forEach(this::requiredTeacherExistence);
        log.info("ADDING... SUBJECT BY ID - {} TO TEACHERS - {}", subjectDTO, teachersDTO.size());
        teachersDTO.forEach(teacher -> addTeacher(subjectDTO.getId(), teacher.getId()));
        log.info("ADDED SUBJECT BY ID - {} TO TEACHERS - {} SUCCESSFULLY", subjectDTO.getId(), teachersDTO.size());
        return teachersDTO;
    }

    private TeacherDTO getTeacherDTO(Teacher newTeacher) {
        return teacherMapper.convertToTeacherDTO(newTeacher);
    }


    private Subject extractSubjectByIdWithRepo(Integer subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find subject by subjectId - %d", subjectId)));
    }

    private Teacher extractTeacherByIdWithRepo(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find teacher by teacherId - %d", teacherId)));
    }

    private void checkToUniqueTeacherInList(Integer subjectId, Integer teacherId) {
        this.findTeachers(subjectId).forEach(teacherFromList -> {
            if (teacherFromList.getId().equals(teacherId)) {
                throw new GlobalNotValidException(format("The subject already has the teacher by id - [%d]", teacherId));
            }});
    }

    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectRepository.existsById(subjectId))
            throw new GlobalNotFoundException(format("Subject by id - %d not exists", subjectId));
    }

    private void requiredTeacherExistence(TeacherDTO teacherDTO) {
        if (!teacherService.existsById(teacherDTO.getId()))
            throw new GlobalNotFoundException(format("Teacher by id - %d not exists", teacherDTO.getId()));
    }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!teacherService.existsById(teacherId))
            throw new GlobalNotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

    private void requireNonNull(Object o) {
        if (o == null) {
            throw new GlobalNotNullException("Can't be null " + o.getClass().getName());
        }
    }
}
