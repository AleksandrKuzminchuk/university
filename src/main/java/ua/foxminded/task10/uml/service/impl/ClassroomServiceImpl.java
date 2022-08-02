package ua.foxminded.task10.uml.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.dto.mapper.ClassroomMapper;
import ua.foxminded.task10.uml.model.Classroom;
import ua.foxminded.task10.uml.repository.ClassroomRepository;
import ua.foxminded.task10.uml.service.ClassroomService;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotFoundException;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotNullException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final ClassroomMapper classroomMapper;

    @Override
    public void saveAll(List<ClassroomDTO> classroomsDTO) {
        requireNonNull(classroomsDTO);
        log.info("SAVING... {} CLASSROOMS", classroomsDTO.size());
        List<Classroom> classrooms = classroomsDTO.stream().map(classroomMapper::map).collect(Collectors.toList());
        classroomRepository.saveAll(classrooms);
        log.info("SAVED {} CLASSROOMS SUCCESSFULLY", classrooms.size());
    }

    @Override
    public void update(ClassroomDTO classroomDTO) {
        requiredClassroomExistence(classroomDTO.getId());
        log.info("UPDATING... CLASSROOM BY ID - {}", classroomDTO.getId());
        Classroom classroom = classroomMapper.map(classroomDTO);
        Classroom updatedClassroom = classroomRepository.save(classroom);
        classroomMapper.map(updatedClassroom);
        log.info("UPDATED {} SUCCESSFULLY", updatedClassroom);
    }

    @Override
    public ClassroomDTO save(ClassroomDTO classroomDTO) {
        log.info("SAVING... {}", classroomDTO);
        Classroom classroom = classroomMapper.map(classroomDTO);
        classroomRepository.save(classroom);
        ClassroomDTO savedClassroomDTO = classroomMapper.map(classroom);
        log.info("SAVED {} SUCCESSFULLY", savedClassroomDTO);
        return savedClassroomDTO;
    }

    @Override
    public ClassroomDTO findById(Integer classroomId) {
        requireNonNull(classroomId);
        requiredClassroomExistence(classroomId);
        log.info("FINDING... CLASSROOM BY ID- {}", classroomId);
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(() -> new GlobalNotFoundException(format("Can't find classroom by classroomId - %d", classroomId)));
        ClassroomDTO classroomDTO = classroomMapper.map(classroom);
        log.info("FOUND CLASSROOM BY ID - {} SUCCESSFULLY", classroomId);
        return classroomDTO;
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
    public List<ClassroomDTO> findAll() {
        log.info("FINDING... ALL CLASSROOMS");
        List<Classroom> result = classroomRepository.findAll(Sort.by(Sort.Order.asc("number")));
        log.info("FOUND {} CLASSROOMS", result.size());
        return result.stream().map(classroomMapper::map).collect(Collectors.toList());
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
    public void delete(ClassroomDTO classroomDTO) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING... ALL CLASSROOMS");
        classroomRepository.deleteAll();
        log.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }

    @Override
    public ClassroomDTO findByNumber(Integer classroomNumber) {
        log.info("FINDING... CLASSROOMS BY NUMBER - {}", classroomNumber);
        Classroom classroom = classroomRepository.findByNumber(classroomNumber).orElseThrow(() -> new GlobalNotFoundException(format("Classroom by number [%d] not found", classroomNumber)));
        ClassroomDTO classroomDTO = classroomMapper.map(classroom);
        log.info("FOUND {} CLASSROOM BY NUMBER - {} SUCCESSFULLY", classroom, classroomNumber);
        return classroomDTO;
    }

    private void requiredClassroomExistence(Integer classroomId) {
        if (!classroomRepository.existsById(classroomId))
            throw new GlobalNotFoundException(format("Classroom by id - %d not exists", classroomId));
    }

    private void requireNonNull(Object o){
        if (o == null){
            throw new GlobalNotNullException("Can't be null " + o.getClass().getName());
        }
    }
}
