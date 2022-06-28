package ua.foxminded.task10.uml.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.repository.SubjectRepository;
import ua.foxminded.task10.uml.repository.TeacherRepository;
import ua.foxminded.task10.uml.service.SubjectService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubjectServiceImpl implements SubjectService {

    SubjectRepository subjectRepository;
    TeacherRepository teacherRepository;

    @Override
    public Subject save(Subject subject) {
        requireNonNull(subject);
        log.info("SAVING... {}", subject);
        Subject result = subjectRepository.save(subject);
        log.info("SAVED {} SUCCESSFULLY", subject);
        return result;
    }

    @Override
    public Subject findById(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("FINDING... SUBJECT BY ID - {}", subjectId);
        Subject result = subjectRepository.findById(subjectId).orElseThrow(() -> new NotFoundException(format("Can't find subject by subjectId - %d", subjectId)));
        log.info("FOUND {} BY ID - {} SUCCESSFULLY", result, subjectId);
        return result;
    }

    @Override
    public List<Subject> findSubjectsByName(Subject subject) {
        requireNonNull(subject);
        log.info("FINDING... SUBJECTS BY NAME {}", subject.getName());
        List<Subject> result = subjectRepository.findSubjectsByName(subject);
        log.info("FOUND {} SUBJECTS BY NAME {}", result.size(), subject.getName());
        return result;
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
    public List<Subject> findAll() {
        log.info("FINDING... ALL SUBJECTS");
        List<Subject> result = subjectRepository.findAll();
        log.info("FOUND {} SUBJECTS SUCCESSFULLY", result.size());
        return result;
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
    public void delete(Subject subject) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Transactional
    @Override
    public void deleteAll() {
        log.info("DELETING... ALL SUBJECTS");
        subjectRepository.deleteAll();
        log.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void deleteTheSubjectTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(teacherId);
        log.info("DELETING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        subjectRepository.deleteTheSubjectTeacher(subjectId, teacherId);
        log.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        requireNonNull(subjects);
        log.info("SAVING {} SUBJECTS", subjects.size());
        subjectRepository.saveAll(subjects);
        log.info("SAVED {} SUBJECTS SUCCESSFULLY", subjects.size());
    }

    @Override
    public void updateSubject(Integer subjectId, Subject subject) {
        requireNonNull(subject);
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("UPDATING... SUBJECT BY ID - {}", subjectId);
        subject.setId(subjectId);
        subjectRepository.save(subject);
        log.info("UPDATED {} SUCCESSFULLY", subject);
    }

    @Override
    public void updateTheSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId) {
        requireNonNull(subjectId);
        requireNonNull(oldTeacherId);
        requireNonNull(newTeacherId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(oldTeacherId);
        requiredTeacherExistence(newTeacherId);
        log.info("UPDATING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
        subjectRepository.updateTheSubjectTeacher(subjectId, oldTeacherId, newTeacherId);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
    }

    @Override
    public List<Teacher> findTeachersBySubject(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("FINDING... TEACHERS BY SUBJECT ID - {}", subjectId);
        List<Teacher> teachers = subjectRepository.findTeachersBySubject(subjectId);
        log.info("FOUND {} TEACHERS BY TEACHER ID - {}", teachers.size(), subjectId);
        return teachers;
    }

    @Override
    public void addSubjectToTeacher(Subject subject, Teacher teacher) {
        requireNonNull(subject);
        requireNonNull(teacher);
        requiredSubjectExistence(subject.getId());
        requiredTeacherExistence(teacher);
        log.info("ADDING... SUBJECT BY ID - {} TO TEACHER BY ID - {}", subject.getId(), teacher.getId());
        subjectRepository.addSubjectToTeacher(subject, teacher);
        log.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subject.getId(), teacher.getId());
    }

    @Override
    public void addSubjectToTeachers(Subject subject, List<Teacher> teachers) {
        requireNonNull(subject);
        requireNonNull(teachers);
        requiredSubjectExistence(subject.getId());
        teachers.forEach(this::requiredTeacherExistence);
        log.info("ADDING... SUBJECT BY ID - {} TO TEACHERS - {}", subject, teachers.size());
        teachers.forEach(teacher -> addSubjectToTeacher(subject, teacher));
        log.info("ADDED SUBJECT BY ID - {} TO TEACHERS - {} SUCCESSFULLY", subject.getId(), teachers.size());
    }

    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectRepository.existsById(subjectId))
            throw new NotFoundException(format("Subject by id - %d not exists", subjectId));
    }

    private void requiredTeacherExistence(Teacher teacher) {
        if (!teacherRepository.existsById(teacher.getId()))
            throw new NotFoundException(format("Teacher by id - %d not exists", teacher.getId()));
    }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!teacherRepository.existsById(teacherId))
            throw new NotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

}
