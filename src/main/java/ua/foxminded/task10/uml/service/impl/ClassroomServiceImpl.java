package ua.foxminded.task10.uml.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.service.ClassroomService;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Component
public class ClassroomServiceImpl implements ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);

    private final ClassroomDao classroomDao;

    @Autowired
    public ClassroomServiceImpl(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        logger.info("SAVING... {} CLASSROOMS", classrooms.size());
        classroomDao.saveAll(classrooms);
        logger.info("SAVED {} CLASSROOMS SUCCESSFULLY", classrooms.size());
    }

    @Override
    public void updateClassroom(Integer classroomId, Classroom classroom) {
        requireNonNull(classroom);
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        logger.info("UPDATING... CLASSROOM BY ID - {}", classroomId);
        classroomDao.updateClassroom(classroomId, classroom);
        logger.info("UPDATED {} SUCCESSFULLY", classroom);
    }

    @Override
    public Classroom save(Classroom classroom) {
        requireNonNull(classroom);
        logger.info("SAVING... {}", classroom);
        Classroom result = classroomDao.save(classroom).orElseThrow(() -> new NotFoundException(format("Can't save %s", classroom)));
        logger.info("SAVED {} SUCCESSFULLY", classroom);
        return result;
    }

    @Override
    public Classroom findById(Integer id) {
        requireNonNull(id);
        requiredClassroomExistence(id);
        logger.info("FINDING... CLASSROOM BY ID- {}", id);
        Classroom result = classroomDao.findById(id).orElseThrow(() -> new NotFoundException(format("Can't find classroom by id - %d", id)));
        logger.info("FOUND CLASSROOM BY ID - {} SUCCESSFULLY", id);
        return result;
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... CLASSROOM EXISTS BY ID - {}", id);
        boolean result = classroomDao.existsById(id);
        logger.info("CLASSROOM BY ID - {} EXISTS - {}", id, result);
        return result;
    }

    @Override
    public List<Classroom> findAll() {
        logger.info("FINDING... ALL CLASSROOMS");
        List<Classroom> result = classroomDao.findAll();
        logger.info("FOUND {} CLASSROOMS", result.size());
        return result;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT CLASSROOMS");
        Long result = classroomDao.count();
        logger.info("FOUND COUNT({}) CLASSROOMS", result);
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        requiredClassroomExistence(id);
        logger.info("DELETING... CLASSROOM BY ID- {}", id);
        classroomDao.deleteById(id);
        logger.info("DELETED CLASSROOMS BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Classroom classroom) {
        requireNonNull(classroom);
        requiredClassroomExistence(classroom);
        logger.info("DELETING... {}", classroom);
        classroomDao.delete(classroom);
        logger.info("DELETED {} SUCCESSFULLY", classroom);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL CLASSROOMS");
        classroomDao.deleteAll();
        logger.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }

    @Override
    public Classroom findClassroomByNumber(Integer classroomNumber) {
        requireNonNull(classroomNumber);
        logger.info("FINDING... CLASSROOM BY NUMBER - {}", classroomNumber);
        Classroom classroom = classroomDao.findClassroomByNumber(classroomNumber).orElseThrow(() -> new NotFoundException(format("Can't find classroom by number - %d", classroomNumber)));
        logger.info("FOUND CLASSROOM BY NUMBER - {} SUCCESSFULLY", classroom);
        return classroom;
    }

    private void requiredClassroomExistence(Integer classroomId) {
        if (!existsById(classroomId))
            throw new NotFoundException(format("Classroom by id - %d not exists", classroomId));
    }

    private void requiredClassroomExistence(Classroom classroom) {
        if (!existsById(findClassroomByNumber(classroom.getNumber()).getId()))
            throw new NotFoundException(format("Classroom by number %d not exists", classroom.getNumber()));
    }
}
