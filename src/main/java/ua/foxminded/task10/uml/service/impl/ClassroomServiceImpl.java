package ua.foxminded.task10.uml.service.impl;

import org.apache.log4j.Logger;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.service.ClassroomService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class ClassroomServiceImpl implements ClassroomService {

    private static final Logger logger = Logger.getLogger(ClassroomServiceImpl.class);

    private final ClassroomDao classroomDao;

    public ClassroomServiceImpl(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        logger.info(format("SAVING... %d CLASSROOMS", classrooms.size()));
        classroomDao.saveAll(classrooms);
        logger.info(format("SAVED %d CLASSROOMS SUCCESSFULLY", classrooms.size()));
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        requireNonNull(classroom);
        logger.info(format("UPDATING... %s", classroom));
        classroomDao.updateClassroom(classroom);
        logger.info(format("UPDATED %s SUCCESSFULLY", classroom));
    }

    @Override
    public Classroom save(Classroom classroom) {
        requireNonNull(classroom);
        logger.info(format("SAVING... %s", classroom));
        Classroom result = classroomDao.save(classroom).orElseThrow(() -> new NotFoundException(format("Can't save %s", classroom)));
        logger.info(format("SAVED %s SUCCESSFULLY", classroom));
        return result;
    }

    @Override
    public Classroom findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... CLASSROOM BY ID- %d", id));
        Classroom result = classroomDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find classroom by id - %d", id)));
        logger.info(format("FOUND CLASSROOM BY ID - %d SUCCESSFULLY", id));
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... CLASSROOM EXISTS BY ID - %d", id));
        boolean result = classroomDao.existsById(id);
        logger.info(format("CLASSROOM BY ID - %d EXISTS - %s", id, result));
        return result;
    }

    @Override
    public List<Classroom> findAll() {
        logger.info("FINDING... ALL CLASSROOMS");
        List<Classroom> result = classroomDao.findAll();
        if (result.isEmpty()){
            logger.info(format("FOUND %d CLASSROOMS", 0));
            return result;
        }
        logger.info(format("FOUND %d CLASSROOMS SUCCESSFULLY", result.size()));
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT CLASSROOMS");
        Long result = classroomDao.count();
        logger.info(format("FOUND COUNT(%d) CLASSROOMS", result));
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING... CLASSROOM BY ID- %d", id));
        classroomDao.deleteById(id);
        logger.info(format("DELETED CLASSROOMS BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Classroom classroom) {
        requireNonNull(classroom);
        logger.info(format("DELETING... %s", classroom));
        classroomDao.delete(classroom);
        logger.info(format("DELETED %s SUCCESSFULLY", classroom));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL CLASSROOMS");
        classroomDao.deleteAll();
        logger.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }
}
