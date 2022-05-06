package ua.foxminded.task10.uml.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class TeacherServiceImpl implements TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherDao teacherDao;
    private final SubjectService subjectService;

    public TeacherServiceImpl(TeacherDao teacherDao, SubjectService subjectService) {
        this.teacherDao = teacherDao;
        this.subjectService = subjectService;
    }

    @Override
    public Teacher save(Teacher teacher) {
        requireNonNull(teacher);
        logger.info("SAVING... {}", teacher);
        Teacher result = teacherDao.save(teacher).orElseThrow(() -> new NotFoundException(format("Can't save %s", teacher)));
        logger.info("SAVED {} SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Teacher findById(Integer id) {
        requireNonNull(id);
        logger.info("FINDING... TEACHER BY ID - {}", id);
        Teacher result = teacherDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find teacher by id - %d", id)));
        logger.info("FOUND {} BY ID - {}", result, id);
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... TEACHER EXISTS BY ID - {}", id);
        boolean result = teacherDao.existsById(id);
        logger.info("TEACHER BY ID - {} EXISTS - {}", id, result);
        return result;
    }

    @Override
    public List<Teacher> findAll() {
        logger.info("FINDING... ALL TEACHERS");
        List<Teacher> result = teacherDao.findAll();
        logger.info("FOUND {} TEACHERS", result.size());
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT TEACHERS");
        Long result = teacherDao.count();
        logger.info("FOUND {} TEACHERS SUCCESSFULLY", result);
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETING... TEACHER BY ID - {}", id);
        teacherDao.deleteById(id);
        logger.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Teacher teacher) {
        requireNonNull(teacher);
        logger.info("DELETING... {}", teacher);
        teacherDao.delete(teacher);
        logger.info("DELETED {} SUCCESSFULLY", teacher);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL TEACHERS");
        teacherDao.deleteAll();
        logger.info("DELETED ALL TEACHERS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Teacher> teachers) {
        requireNonNull(teachers);
        logger.info("SAVING... {} TEACHERS", teachers.size());
        teacherDao.saveAll(teachers);
        logger.info("SAVED {} TEACHERS SUCCESSFULLY", teachers.size());
    }

    @Override
    public void updateTeacher(Teacher teacher) {
        requireNonNull(teacher);
        logger.info("UPDATING... {}", teacher);
        teacherDao.updateTeacher(teacher);
        logger.info("UPDATED {} SUCCESSFULLY", teacher);
    }

    @Override
    public void addTeacherToSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(teacherId);
        logger.info("ADDING... TEACHER BY ID - {} TO SUBJECT BY ID - {}", teacherId, subjectId);
        teacherDao.addTeacherToSubject(teacherId, subjectId);
        logger.info("ADDED TEACHER BT ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, subjectId);
    }

    @Override
    public void addTeacherToSubjects(Integer teacherId, List<Subject> subjects) {
        requireNonNull(teacherId);
        requireNonNull(subjects);
        requiredTeacherExistence(teacherId);
        subjects.forEach(subject -> requiredSubjectExistence(subject.getId()));
        logger.info("ADDING... TEACHER BY ID - {} TO SUBJECTS {}", teacherId, subjects.size());
        teacherDao.addTeacherToSubjects(teacherId, subjects);
        logger.info("ADDED TEACHER BY ID - {} TO SUBJECTS {} SUCCESSFULLY", teacherId, subjects.size());
    }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!existsById(teacherId))
            throw new NotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectService.existsById(subjectId))
            throw new NotFoundException(format("Subject by id - %d not exists", subjectId));
    }
}
