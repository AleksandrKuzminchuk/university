package ua.foxminded.task10.uml.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.SubjectDao;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Subject;
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
        requireNonNull(subject);
        requiredSubjectExistence(subject.getId());
        logger.info("DELETING... {}", subject);
        subjectDao.delete(subject);
        logger.info("DELETED {} SUCCESSFULLY", subject);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL SUBJECTS");
        subjectDao.deleteAll();
        logger.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        requireNonNull(subjects);
        logger.info("SAVING {} SUBJECTS", subjects.size());
        subjectDao.saveAll(subjects);
        logger.info("SAVED {} SUBJECTS SUCCESSFULLY", subjects.size());
    }

    @Override
    public void updateSubject(Subject subject) {
        requireNonNull(subject);
        requiredSubjectExistence(subject.getId());
        logger.info("UPDATING... {}", subject);
        subjectDao.updateSubject(subject);
        logger.info("UPDATED {} SUCCESSFULLY", subject);
    }

    @Override
    public List<Subject> findTeacherSubjects(Integer teacherId) {
        requireNonNull(teacherId);
        if (!teacherDao.existsById(teacherId))
            throw new NotFoundException(format("Can't find teacher by id - %d", teacherId));
        logger.info("FINDING... SUBJECTS BY TEACHER ID - {}", teacherId);
        List<Subject> result = subjectDao.findTeacherSubjects(teacherId);
        logger.info("FOUND {} SUBJECTS BY TEACHER ID - {}", result, teacherId);
        return result;
    }
    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectDao.existsById(subjectId))
            throw new NotFoundException(format("Subject by id - %d not exists", subjectId));
    }


}
