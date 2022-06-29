package ua.foxminded.task10.uml.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.service.ClassroomService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClassroomServiceImpl implements ClassroomService {

    ClassroomDao classroomDao;

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        log.info("SAVING... {} CLASSROOMS", classrooms.size());
        classroomDao.saveAll(classrooms);
        log.info("SAVED {} CLASSROOMS SUCCESSFULLY", classrooms.size());
    }

    @Override
    public void updateClassroom(Integer classroomId, Classroom classroom) {
        requireNonNull(classroom);
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        log.info("UPDATING... CLASSROOM BY ID - {}", classroomId);
        classroomDao.updateClassroom(classroomId, classroom);
        log.info("UPDATED {} SUCCESSFULLY", classroom);
    }

    @Override
    public Classroom save(Classroom classroom) {
        requireNonNull(classroom);
        log.info("SAVING... {}", classroom);
        Classroom result = classroomDao.save(classroom).orElseThrow(() -> new NotFoundException(format("Can't save %s", classroom)));
        log.info("SAVED {} SUCCESSFULLY", classroom);
        return result;
    }

    @Override
    public Classroom findById(Integer classroomId) {
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        log.info("FINDING... CLASSROOM BY ID- {}", classroomId);
        Classroom result = classroomDao.findById(classroomId).orElseThrow(() -> new NotFoundException(format("Can't find classroom by classroomId - %d", classroomId)));
        log.info("FOUND CLASSROOM BY ID - {} SUCCESSFULLY", classroomId);
        return result;
    }

    @Override
    public boolean existsById(Integer classroomId) {
        requireNonNull(classroomId);
        log.info("CHECKING... CLASSROOM EXISTS BY ID - {}", classroomId);
        boolean result = classroomDao.existsById(classroomId);
        log.info("CLASSROOM BY ID - {} EXISTS - {}", classroomId, result);
        return result;
    }

    @Override
    public List<Classroom> findAll() {
        log.info("FINDING... ALL CLASSROOMS");
        List<Classroom> result = classroomDao.findAll();
        log.info("FOUND {} CLASSROOMS", result.size());
        return result;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT CLASSROOMS");
        Long result = classroomDao.count();
        log.info("FOUND COUNT({}) CLASSROOMS", result);
        return result;
    }

    @Override
    public void deleteById(Integer classroomId) {
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        log.info("DELETING... CLASSROOM BY ID- {}", classroomId);
        classroomDao.deleteById(classroomId);
        log.info("DELETED CLASSROOMS BY ID - {} SUCCESSFULLY", classroomId);
    }

    @Override
    public void delete(Classroom classroom) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL CLASSROOMS");
        classroomDao.deleteAll();
        log.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }

    @Override
    public List<Classroom> findClassroomsByNumber(Integer classroomNumber) {
        requireNonNull(classroomNumber);
        log.info("FINDING... CLASSROOMS BY NUMBER - {}", classroomNumber);
        List<Classroom> classrooms = classroomDao.findClassroomsByNumber(classroomNumber);
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", classrooms.size(), classroomNumber);
        return classrooms;
    }

    private void requiredClassroomExistence(Integer classroomId) {
        if (!existsById(classroomId))
            throw new NotFoundException(format("Classroom by id - %d not exists", classroomId));
    }
}
