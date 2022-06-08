package ua.foxminded.task10.uml.service.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.SubjectDao;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Component
public class SubjectServiceImpl implements SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private final SubjectDao subjectDao;
    private final TeacherDao teacherDao;

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao, TeacherDao teacherDao) {
        this.subjectDao = subjectDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public Subject save(Subject subject) {
        requireNonNull(subject);
        logger.info("SAVING... {}", subject);
        Subject result = subjectDao.save(subject).orElseThrow(() -> new NotFoundException(format("Can't save %s", subject)));
        logger.info("SAVED {} SUCCESSFULLY", subject);
        return result;
    }

    @Override
    public Subject findById(Integer id) {
        requireNonNull(id);
        requiredSubjectExistence(id);
        logger.info("FINDING... SUBJECT BY ID - {}", id);
        Subject result = subjectDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find subject by id - %d", id)));
        logger.info("FOUND {} BY ID - {} SUCCESSFULLY", result, id);
        return result;
    }

    @Override
    public Subject findSubjectByName(Subject subject) {
        requireNonNull(subject);
        logger.info("FINDING... SUBJECT BY NAME {}", subject.getName());
        Subject result = subjectDao.findSubjectByName(subject).orElseThrow(() -> new NotFoundException(format("Can't find subject by name %s", subject.getName())));
        logger.info("FOUND SUBJECT BY NAME {}", result.getName());
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... SUBJECT EXISTS BY ID - {}", id);
        boolean result = subjectDao.existsById(id);
        logger.info("SUBJECT BY ID - {} EXISTS - {}", id, result);
        return result;
    }

    @Override
    public List<Subject> findAll() {
        logger.info("FINDING... ALL SUBJECTS");
        List<Subject> result = subjectDao.findAll();
        logger.info("FOUND {} SUBJECTS SUCCESSFULLY", result.size());
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT SUBJECTS");
        Long result = subjectDao.count();
        logger.info("FOUND {} SUBJECTS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        requiredSubjectExistence(id);
        logger.info("DELETING SUBJECT BY ID - {}", id);
        subjectDao.deleteById(id);
        logger.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Subject subject) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL SUBJECTS");
        subjectDao.deleteAll();
        logger.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void deleteTheSubjectTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(teacherId);
        logger.info("DELETING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        subjectDao.deleteTheSubjectTeacher(subjectId, teacherId);
        logger.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        requireNonNull(subjects);
        logger.info("SAVING {} SUBJECTS", subjects.size());
        subjectDao.saveAll(subjects);
        logger.info("SAVED {} SUBJECTS SUCCESSFULLY", subjects.size());
    }

    @Override
    public void updateSubject(Integer subjectId, Subject subject) {
        requireNonNull(subject);
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        logger.info("UPDATING... SUBJECT BY ID - {}", subjectId);
        subjectDao.updateSubject(subjectId, subject);
        logger.info("UPDATED {} SUCCESSFULLY", subject);
    }

    @Override
    public void updateTheSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId) {
        requireNonNull(subjectId);
        requireNonNull(oldTeacherId);
        requireNonNull(newTeacherId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(oldTeacherId);
        requiredTeacherExistence(newTeacherId);
        logger.info("UPDATING... THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
        subjectDao.updateTheSubjectTeacher(subjectId, oldTeacherId, newTeacherId);
        logger.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
    }

    @Override
    public List<Teacher> findTeachersBySubject(Integer subjectId) {
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        logger.info("FINDING... TEACHERS BY SUBJECT ID - {}", subjectId);
        List<Teacher> teachers = subjectDao.findTeachersBySubject(subjectId);
        logger.info("FOUND {} TEACHERS BY TEACHER ID - {}", teachers.size(), subjectId);
        return teachers;
    }

    @Override
    public void addSubjectToTeacher(Subject subjectId, Teacher teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(teacherId);
        logger.info("ADDING... SUBJECT BY ID - {} TO TEACHER BY ID - {}", subjectId.getId(), teacherId.getId());
        subjectDao.addSubjectToTeacher(subjectId, teacherId);
        logger.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId.getId(), teacherId.getId());
    }

    @Override
    public void addSubjectToTeachers(Subject subjectId, List<Teacher> teachers) {
        requireNonNull(subjectId);
        requireNonNull(teachers);
        requiredSubjectExistence(subjectId);
        teachers.forEach(this::requiredTeacherExistence);
        logger.info("ADDING... SUBJECT BY ID - {} TO TEACHERS - {}", subjectId, teachers.size());
        teachers.forEach(teacher -> addSubjectToTeacher(subjectId, teacher));
        logger.info("ADDED SUBJECT BY ID - {} TO TEACHERS - {} SUCCESSFULLY", subjectId.getId(), teachers.size());
    }

    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectDao.existsById(subjectId))
            throw new NotFoundException(format("Subject by id - %d not exists", subjectId));
    }

    private void requiredSubjectExistence(Subject subject) {
        if (!subjectDao.existsById(findSubjectByName(subject).getId()))
            throw new NotFoundException(format("Subject by name - %s not exists", subject.getName()));
    }

    private void requiredTeacherExistence(Teacher teacher) {
        if (!teacherDao.existsById(teacher.getId()))
            throw new NotFoundException(format("Teacher by id - %d not exists", teacher.getId()));
    }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!teacherDao.existsById(teacherId))
            throw new NotFoundException(format("Teacher by id - %d not exists", teacherId));
    }


}
