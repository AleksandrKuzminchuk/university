package ua.foxminded.task10.uml.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.repository.TeacherRepository;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;
import ua.foxminded.task10.uml.util.GlobalNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final SubjectService subjectService;

    public TeacherServiceImpl(TeacherRepository teacherRepository, @Lazy SubjectService subjectService) {
        this.teacherRepository = teacherRepository;
        this.subjectService = subjectService;
    }

    @Override
    public Teacher save(Teacher teacher) {
        log.info("SAVING... {}", teacher);
        teacherRepository.save(teacher);
        log.info("SAVED {} SUCCESSFULLY", teacher);
        return teacher;
    }

    @Override
    public Teacher findById(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("FINDING... TEACHER BY ID - {}", teacherId);
        Teacher result = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find teacher by teacherId - %d", teacherId)));
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
        List<Teacher> result = teacherRepository.findAll(Sort.by(Sort.Order.asc("firstName")));
        log.info("FOUND {} TEACHERS", result.size());
        return result;
    }

    @Override
    public List<Teacher> findTeachersByNameOrSurname(Teacher teacher) {
        log.info("FINDING... TEACHERS {}", teacher);
        List<Teacher> result = teacherRepository.findTeachersByFirstNameOrLastName(teacher.getFirstName(), teacher.getLastName(), Sort.by(Sort.Order.asc("firstName")));
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
    public void deleteFromTeacherSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredTeacherExistence(teacherId);
        log.info("DELETING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        Teacher teacher = findById(teacherId);
        Subject subjectToRemove = subjectService.findById(subjectId);
        teacher.getSubjects().remove(subjectToRemove);
        subjectToRemove.getTeachers().remove(teacher);
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
    public Teacher update(Teacher teacher) {
        requiredTeacherExistence(teacher.getId());
        log.info("UPDATING... TEACHER BY ID - {}", teacher.getId());
        Teacher updatedTeacher = teacherRepository.save(teacher);
        log.info("UPDATED TEACHER BY ID - {} SUCCESSFULLY", teacher.getId());
        return updatedTeacher;
    }

    @Override
    public void updateAtTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId) {
        requireNonNull(teacherId);
        requireNonNull(oldSubjectId);
        requireNonNull(newSubjectId);
        requiredTeacherExistence(teacherId);
        log.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubjectId);
        Teacher teacher = findById(teacherId);
        Subject oldSubject = subjectService.findById(oldSubjectId);
        Subject newSubject = subjectService.findById(newSubjectId);
        teacher.getSubjects().remove(oldSubject);
        teacher.getSubjects().add(newSubject);
        log.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, oldSubjectId, newSubjectId);
    }

    @Override
    public void addTeacherToSubject(Teacher teacher, Subject subject) {
        requireNonNull(teacher);
        requireNonNull(subject);
        requiredTeacherExistence(teacher.getId());
        log.info("ADDING... TEACHER BY ID - {} TO SUBJECT BY ID - {}", teacher.getId(), subject.getId());
        Teacher teacherToBeSave = findById(teacher.getId());
        Subject subjectToBeSave = subjectService.findById(subject.getId());
        teacherToBeSave.getSubjects().add(subjectToBeSave);
        log.info("ADDED TEACHER BT ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacher.getId(), subject.getId());
    }

    @Override
    public void addTeacherToSubjects(Teacher teacher, List<Subject> subjects) {
        requireNonNull(teacher);
        requireNonNull(subjects);
        requiredTeacherExistence(teacher.getId());
        subjects.forEach(subject -> subjectService.existsById(subject.getId()));
        log.info("ADDING... TEACHER BY ID - {} TO SUBJECTS {}", teacher.getId(), subjects.size());
        subjects.forEach(subject -> addTeacherToSubject(teacher, subject));
        log.info("ADDED TEACHER BY ID - {} TO SUBJECTS {} SUCCESSFULLY", teacher.getId(), subjects.size());
    }

    @Override
    public List<Subject> findSubjectsByTeacher(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        log.info("FINDING... SUBJECTS BY TEACHER ID - {}", teacherId);
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new GlobalNotFoundException(format("Can't find teacher by teacherId - %d", teacherId)));
        log.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", teacher.getSubjects().size(), teacherId);
        return teacher.getSubjects();
     }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!teacherRepository.existsById(teacherId))
            throw new GlobalNotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

}
