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
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TeacherServiceImpl implements TeacherService {

    TeacherRepository teacherRepository;
    SubjectRepository subjectRepository;

    @Override
    public Teacher save(Teacher teacher) {
        requireNonNull(teacher);
        log.info("SAVING... {}", teacher);
        Teacher result = teacherRepository.save(teacher);
        log.info("SAVED {} SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Teacher findById(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("FINDING... TEACHER BY ID - {}", teacherId);
        Teacher result = teacherRepository.findById(teacherId).orElseThrow(() -> new NotFoundException(format("Can't find teacher by teacherId - %d", teacherId)));
        log.info("FOUND {} BY ID - {}", result, teacherId);
        return result;
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
    public List<Teacher> findAll() {
        log.info("FINDING... ALL TEACHERS");
        List<Teacher> result = teacherRepository.findAll();
        log.info("FOUND {} TEACHERS", result.size());
        return result;
    }

    @Override
    public List<Teacher> findTeachersByNameOrSurname(Teacher teacher) {
        requireNonNull(teacher);
        log.info("FINDING... TEACHERS {}", teacher);
        List<Teacher> result = teacherRepository.findTeachersByNameOrSurname(teacher);
        log.info("FOUND {} TEACHERS BY {} SUCCESSFULLY", result.size(), teacher);
        return result;
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
    public void delete(Teacher teacher) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL TEACHERS");
        teacherRepository.deleteAll();
        log.info("DELETED ALL TEACHERS SUCCESSFULLY");
    }

    @Override
    public void deleteTheTeacherSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(subjectId);
        log.info("DELETING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        teacherRepository.deleteTheTeacherSubject(teacherId, subjectId);
        log.info("DELETED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} SUCCESSFULLY", teacherId, subjectId);
    }

    @Override
    public void saveAll(List<Teacher> teachers) {
        requireNonNull(teachers);
        log.info("SAVING... {} TEACHERS", teachers.size());
        teacherRepository.saveAll(teachers);
        log.info("SAVED {} TEACHERS SUCCESSFULLY", teachers.size());
    }

    @Override
    public void updateTeacher(Integer teacherId, Teacher teacher) {
        requireNonNull(teacher);
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("UPDATING... TEACHER BY ID - {}", teacherId);
        teacher.setId(teacherId);
        teacherRepository.save(teacher);
        log.info("UPDATED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
    }

    @Override
    public void updateTheTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId) {
        requireNonNull(teacherId);
        requireNonNull(oldSubjectId);
        requireNonNull(newSubjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(newSubjectId);
        requiredSubjectExistence(oldSubjectId);
        log.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubjectId);
        teacherRepository.updateTheTeacherSubject(teacherId, oldSubjectId, newSubjectId);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, oldSubjectId, newSubjectId);
    }

    @Override
    public void addTeacherToSubject(Teacher teacher, Subject subject) {
        requireNonNull(teacher);
        requireNonNull(subject);
        requiredSubjectExistence(subject.getId());
        requiredTeacherExistence(teacher.getId());
        log.info("ADDING... TEACHER BY ID - {} TO SUBJECT BY ID - {}", teacher.getId(), subject.getId());
        teacherRepository.addTeacherToSubject(teacher, subject);
        log.info("ADDED TEACHER BT ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacher.getId(), subject.getId());
    }

    @Override
    public void addTeacherToSubjects(Teacher teacher, List<Subject> subjects) {
        requireNonNull(teacher);
        requireNonNull(subjects);
        requiredTeacherExistence(teacher.getId());
        subjects.forEach(subject -> requiredSubjectExistence(subject.getId()));
        log.info("ADDING... TEACHER BY ID - {} TO SUBJECTS {}", teacher.getId(), subjects.size());
        teacherRepository.addTeacherToSubjects(teacher, subjects);
        log.info("ADDED TEACHER BY ID - {} TO SUBJECTS {} SUCCESSFULLY", teacher.getId(), subjects.size());
    }

    @Override
    public List<Subject> findSubjectsByTeacherId(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("FINDING... SUBJECTS BY TEACHER ID - {}", teacherId);
        List<Subject> subjects = teacherRepository.findSubjectsByTeacherId(teacherId);
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return subjects;
     }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!existsById(teacherId))
            throw new NotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectRepository.existsById(subjectId))
            throw new NotFoundException(format("Subject by id - %d not exists", subjectId));
    }
}
