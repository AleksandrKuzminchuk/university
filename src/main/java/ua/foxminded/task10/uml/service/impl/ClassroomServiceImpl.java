package ua.foxminded.task10.uml.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.exceptions.NotFoundException;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.repository.ClassroomRepository;
import ua.foxminded.task10.uml.service.ClassroomService;
import ua.foxminded.task10.uml.util.GlobalNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClassroomServiceImpl implements ClassroomService {

    ClassroomRepository classroomRepository;

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        log.info("SAVING... {} CLASSROOMS", classrooms.size());
        classroomRepository.saveAll(classrooms);
        log.info("SAVED {} CLASSROOMS SUCCESSFULLY", classrooms.size());
    }

    @Override
    public void updateClassroom(Integer classroomId, Classroom classroom) {
        requireNonNull(classroom);
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        log.info("UPDATING... CLASSROOM BY ID - {}", classroomId);
        classroom.setId(classroomId);
        classroomRepository.save(classroom);
        log.info("UPDATED {} SUCCESSFULLY", classroom);
    }

    @Override
    public Classroom save(Classroom classroom) {
        requireNonNull(classroom);
        log.info("SAVING... {}", classroom);
        classroomRepository.save(classroom);
        log.info("SAVED {} SUCCESSFULLY", classroom);
        return classroom;
    }

    @Override
    public Classroom findById(Integer classroomId) {
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        log.info("FINDING... CLASSROOM BY ID- {}", classroomId);
        Classroom result = classroomRepository.findById(classroomId).orElseThrow(() -> new GlobalNotFoundException(format("Can't find classroom by classroomId - %d", classroomId)));
        log.info("FOUND CLASSROOM BY ID - {} SUCCESSFULLY", classroomId);
        return result;
    }

    @Override
    public boolean existsById(Integer classroomId) {
        requireNonNull(classroomId);
        log.info("CHECKING... CLASSROOM EXISTS BY ID - {}", classroomId);
        boolean result = classroomRepository.existsById(classroomId);
        log.info("CLASSROOM BY ID - {} EXISTS - {}", classroomId, result);
        return result;
    }

    @Override
    public List<Classroom> findAll() {
        log.info("FINDING... ALL CLASSROOMS");
        List<Classroom> result = classroomRepository.findAll(Sort.by(Sort.Order.asc("number")));
        log.info("FOUND {} CLASSROOMS", result.size());
        return result;
    }

    @Override
    public Long count() {
        log.info("FINDING... COUNT CLASSROOMS");
        Long result = classroomRepository.count();
        log.info("FOUND COUNT({}) CLASSROOMS", result);
        return result;
    }

    @Override
    public void deleteById(Integer classroomId) {
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        log.info("DELETING... CLASSROOM BY ID- {}", classroomId);
        classroomRepository.deleteById(classroomId);
        log.info("DELETED CLASSROOMS BY ID - {} SUCCESSFULLY", classroomId);
    }

    @Override
    public void delete(Classroom classroom) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL CLASSROOMS");
        classroomRepository.deleteAll();
        log.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }

    @Override
    public Classroom findClassroomByNumber(Classroom classroom) {
        requireNonNull(classroom);
        log.info("FINDING... CLASSROOMS BY NUMBER - {}", classroom.getNumber());
        Classroom result = classroomRepository.findOne(Example.of(classroom)).orElseThrow(() -> new GlobalNotFoundException(format("Classroom by number [%d] not found", classroom.getNumber())));
        log.info("FOUND {} CLASSROOM BY NUMBER - {} SUCCESSFULLY", result, classroom.getNumber());
        return result;
    }

    private void requiredClassroomExistence(Integer classroomId) {
        if (!classroomRepository.existsById(classroomId))
            throw new NotFoundException(format("Classroom by id - %d not exists", classroomId));
    }
}
