package ua.foxminded.task10.uml.service.impl;

import org.apache.log4j.Logger;
import ua.foxminded.task10.uml.dao.SubjectDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class SubjectServiceImpl implements SubjectService {

    private static final Logger logger = Logger.getLogger(StudentServiceImpl.class);

    private final SubjectDao subjectDao;
    private final TeacherService teacherService;

    public SubjectServiceImpl(SubjectDao subjectDao, TeacherService teacherService) {
        this.subjectDao = subjectDao;
        this.teacherService = teacherService;
    }

    @Override
    public Subject save(Subject subject) {
        requireNonNull(subject);
        logger.info(format("SAVING... %s", subject));
        Subject result = subjectDao.save(subject).orElseThrow(()-> new NotFoundException(format("Can't save %s", subject)));
        logger.info(format("SAVED %s SUCCESSFULLY", subject));
        return result;
    }

    @Override
    public Subject findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... SUBJECT BY ID - %d", id));
        Subject result = subjectDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find subject by id - %d", id)));
        logger.info(format("FOUND %s BY ID - %d SUCCESSFULLY", result, id));
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... SUBJECT EXISTS BY ID - %d", id));
        boolean result = subjectDao.existsById(id);
        logger.info(format("SUBJECT BY ID - %d EXISTS - %s", id, result));
        return result;
    }

    @Override
    public List<Subject> findAll() {
        logger.info("FINDING... ALL SUBJECTS");
        List<Subject> result = subjectDao.findAll();
        if (result.isEmpty()){
            logger.info(format("FOUND %d SUBJECTS", 0));
            return result;
        }
        logger.info(format("FOUND %d SUBJECTS SUCCESSFULLY", result.size()));
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT SUBJECTS");
        Long result = subjectDao.count();
        logger.info(format("FOUND %d SUBJECTS SUCCESSFULLY", result));
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING SUBJECT BY ID - %d", id));
        subjectDao.deleteById(id);
        logger.info(format("DELETED SUBJECT BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Subject subject) {
        requireNonNull(subject);
        logger.info(format("DELETING... %s", subject));
        subjectDao.delete(subject);
        logger.info(format("DELETED %s SUCCESSFULLY", subject));
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
        logger.info(format("SAVING %d SUBJECTS", subjects.size()));
        subjectDao.saveAll(subjects);
        logger.info(format("SAVED %d SUBJECTS SUCCESSFULLY", subjects.size()));
    }

    @Override
    public void updateSubject(Subject subject) {
        requireNonNull(subject);
        logger.info(format("UPDATING... %s", subject));
        subjectDao.updateSubject(subject);
        logger.info(format("UPDATED %s SUCCESSFULLY", subject));
    }

    @Override
    public List<Subject> findTeacherSubjects(Integer teacherId) {
        requireNonNull(teacherId);
        getNotFoundException(teacherId);
        logger.info(format("FINDING... SUBJECTS BY TEACHER ID - %d", teacherId));
        List<Subject> result = subjectDao.findTeacherSubjects(teacherId);
        if (result.isEmpty()){
            logger.info(format("FOUND %d SUBJECTS BY TEACHER ID- %d", 0, teacherId));
            return result;
        }
        logger.info(format("FOUND %d SUBJECTS BY TEACHER ID - %d SUCCESSFULLY", result.size(), teacherId));
        return result;
    }

    private void getNotFoundException(Integer teacherId){
        if (!teacherService.existsById(teacherId)){
            throw new NotFoundException(format("Can't find teacher by id - %d", teacherId));
        }
    }
}
