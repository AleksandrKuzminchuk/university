package ua.foxminded.task10.uml.service.impl;

import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(TeacherServiceImpl.class);

    private final TeacherDao teacherDao;
    private final SubjectService subjectService;

    public TeacherServiceImpl(TeacherDao teacherDao, SubjectService subjectService) {
        this.teacherDao = teacherDao;
        this.subjectService = subjectService;
    }

    @Override
    public Teacher save(Teacher teacher) {
        requireNonNull(teacher);
        logger.info(format("SAVING... %s", teacher));
        Teacher result = teacherDao.save(teacher).orElseThrow(() -> new NotFoundException(format("Can't save %s", teacher)));
        logger.info(format("SAVED %s SUCCESSFULLY", result));
        return result;
    }

    @Override
    public Teacher findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... TEACHER BY ID - %d", id));
        Teacher result = teacherDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find teacher by id - %d", id)));
        logger.info(format("FOUND %s BY ID - %d", result, id));
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... TEACHER EXISTS BY ID - %d", id));
        boolean result = teacherDao.existsById(id);
        logger.info(format("TEACHER BY ID - %d EXISTS - %s", id, result));
        return result;
    }

    @Override
    public List<Teacher> findAll() {
        logger.info("FINDING... ALL TEACHERS");
        List<Teacher> result = teacherDao.findAll();
        logger.info(format("FOUND %d TEACHERS", result.size()));
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT TEACHERS");
        Long result = teacherDao.count();
        logger.info(format("FOUND %d TEACHERS SUCCESSFULLY", result));
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING... TEACHER BY ID - %d", id));
        teacherDao.deleteById(id);
        logger.info(format("DELETED TEACHER BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Teacher teacher) {
        requireNonNull(teacher);
        logger.info(format("DELETING... %s", teacher));
        teacherDao.delete(teacher);
        logger.info(format("DELETED %s SUCCESSFULLY", teacher));
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
        logger.info(format("SAVING... %d TEACHERS", teachers.size()));
        teacherDao.saveAll(teachers);
        logger.info(format("SAVED %d TEACHERS SUCCESSFULLY", teachers.size()));
    }

    @Override
    public void updateTeacher(Teacher teacher) {
        requireNonNull(teacher);
        logger.info(format("UPDATING... %s", teacher));
        teacherDao.updateTeacher(teacher);
        logger.info(format("UPDATED %s SUCCESSFULLY", teacher));
    }

    @Override
    public void addTeacherToSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId);
        requiredTeacherExistence(teacherId);
        logger.info(format("ADDING... TEACHER BY ID - %d TO SUBJECT BY ID - %d", teacherId, subjectId));
        teacherDao.addTeacherToSubject(teacherId, subjectId);
        logger.info(format("ADDED TEACHER BT ID - %d TO SUBJECT BY ID - %d SUCCESSFULLY", teacherId, subjectId));
    }

    @Override
    public void addTeacherToSubjects(Integer teacherId, List<Subject> subjects) {
        requireNonNull(teacherId);
        requireNonNull(subjects);
        subjects.forEach(subject -> requiredSubjectExistence(subject.getId()));
        logger.info(format("ADDING... TEACHER BY ID - %d TO SUBJECTS %d", teacherId, subjects.size()));
        teacherDao.addTeacherToSubjects(teacherId, subjects);
        logger.info(format("ADDED TEACHER BY ID - %d TO SUBJECTS %d SUCCESSFULLY", teacherId, subjects.size()));
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
