package ua.foxminded.task10.uml.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.repository.SubjectRepository;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;
import ua.foxminded.task10.uml.util.GlobalNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {


    private final TeacherService teacherService;
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(@Lazy TeacherService teacherService, SubjectRepository subjectRepository) {
        this.teacherService = teacherService;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Subject save(Subject subject) {
        requireNonNull(subject);
        log.info("SAVING... {}", subject);
        subjectRepository.save(new Subject(subject.getName().toUpperCase()));
        log.info("SAVED {} SUCCESSFULLY", subject);
        return subject;
    }

    @Override
    public Subject findById(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("FINDING... SUBJECT BY ID - {}", subjectId);
        Subject result = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find subject by subjectId - %d", subjectId)));
        log.info("FOUND {} BY ID - {} SUCCESSFULLY", result, subjectId);
        return result;
    }

    @Override
    public Subject findByName(Subject subject) {
        log.info("FINDING... SUBJECT BY NAME {}", subject.getName());
        Subject result = subjectRepository.findSubjectsByName(subject.getName())
                .orElseThrow(() -> new GlobalNotFoundException(format("Can't find subject by name - %s", subject.getName())));
        log.info("FOUND {} SUBJECT BY NAME {}", result, subject.getName());
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
    public void deleteFromSubjectTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(teacherId);
        log.info("DELETING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        Subject subject = findById(subjectId);
        Teacher teacherToRemove = teacherService.findById(teacherId);
        subject.getTeachers().remove(teacherToRemove);
        teacherToRemove.getSubjects().remove(subject);
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
    public Subject update(Subject subject) {
        requiredSubjectExistence(subject.getId());
        log.info("UPDATING... SUBJECT BY ID - {}", subject.getId());
        Subject updatedSubject = subjectRepository.save(subject);
        log.info("UPDATED {} SUCCESSFULLY", updatedSubject);
        return updatedSubject;
    }

    @Override
    public void updateAtSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId) {
        requireNonNull(subjectId);
        requireNonNull(oldTeacherId);
        requireNonNull(newTeacherId);
        requiredTeacherExistence(oldTeacherId);
        requiredTeacherExistence(newTeacherId);
        log.info("UPDATING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
        Subject subject = findById(subjectId);
        Teacher oldTeacher = teacherService.findById(oldTeacherId);
        Teacher newTeacher = teacherService.findById(newTeacherId);
        subject.getTeachers().remove(oldTeacher);
        subject.getTeachers().add(newTeacher);
        log.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
    }

    @Override
    public List<Teacher> findTeachersBySubject(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        log.info("FINDING... TEACHERS BY SUBJECT ID - {}", subjectId);
        Subject subject = findById(subjectId);
        log.info("FOUND {} TEACHERS BY TEACHER ID - {}", subject.getTeachers().size(), subjectId);
        return subject.getTeachers();
    }

    @Override
    public void addSubjectToTeacher(Subject subject, Teacher teacher) {
        requireNonNull(subject);
        requireNonNull(teacher);
        requiredTeacherExistence(teacher);
        log.info("ADDING... SUBJECT BY ID - {} TO TEACHER BY ID - {}", subject.getId(), teacher.getId());
        Teacher teacherToBeSave = teacherService.findById(teacher.getId());
        Subject subjectToBeSave = findById(subject.getId());
        subjectToBeSave.getTeachers().add(teacherToBeSave);
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
            throw new GlobalNotFoundException(format("Subject by id - %d not exists", subjectId));
    }

    private void requiredTeacherExistence(Teacher teacher) {
        if (!teacherService.existsById(teacher.getId()))
            throw new GlobalNotFoundException(format("Teacher by id - %d not exists", teacher.getId()));
    }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!teacherService.existsById(teacherId))
            throw new GlobalNotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

}
