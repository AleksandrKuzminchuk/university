package ua.foxminded.task10.uml.service.impl;

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
public class TeacherServiceImpl implements TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherDao teacherDao;
    private final SubjectDao subjectDao;

    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao, SubjectDao subjectDao) {
        this.teacherDao = teacherDao;
        this.subjectDao = subjectDao;
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
        requiredTeacherExistence(id);
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
    public Teacher findTeacherByNameSurname(Teacher teacher) {
        requireNonNull(teacher);
        logger.info("FINDING... TEACHER {}", teacher);
        Teacher result = teacherDao.findTeacherByNameSurname(teacher).orElseThrow(() -> new NotFoundException(format("Can't find teacher %s", teacher)));
        logger.info("FOUND TEACHER {} SUCCESSFULLY", teacher);
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
        requiredTeacherExistence(id);
        logger.info("DELETING... TEACHER BY ID - {}", id);
        teacherDao.deleteById(id);
        logger.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Teacher teacher) {
        requireNonNull(teacher);
        requiredTeacherExistence(teacher);
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
    public void deleteTheTeacherSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(subjectId);
        logger.info("DELETING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        teacherDao.deleteTheTeacherSubject(teacherId, subjectId);
        logger.info("DELETED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} SUCCESSFULLY", teacherId, subjectId);
    }

    @Override
    public void saveAll(List<Teacher> teachers) {
        requireNonNull(teachers);
        logger.info("SAVING... {} TEACHERS", teachers.size());
        teacherDao.saveAll(teachers);
        logger.info("SAVED {} TEACHERS SUCCESSFULLY", teachers.size());
    }

    @Override
    public void updateTeacher(Integer teacherId, Teacher teacher) {
        requireNonNull(teacher);
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        logger.info("UPDATING... TEACHER BY ID - {}", teacherId);
        teacherDao.updateTeacher(teacherId, teacher);
        logger.info("UPDATED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
    }

    @Override
    public void updateTheTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId) {
        requireNonNull(teacherId);
        requireNonNull(oldSubjectId);
        requireNonNull(newSubjectId);
        requiredTeacherExistence(teacherId);
        requiredSubjectExistence(newSubjectId);
        requiredSubjectExistence(oldSubjectId);
        logger.info("UPDATING... THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubjectId);
        teacherDao.updateTheTeacherSubject(teacherId, oldSubjectId, newSubjectId);
        logger.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, oldSubjectId, newSubjectId);
    }

    @Override
    public void addTeacherToSubject(Teacher teacherId, Subject subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        requiredSubjectExistence(subjectId.getId());
        requiredTeacherExistence(teacherId.getId());
        logger.info("ADDING... TEACHER BY ID - {} TO SUBJECT BY ID - {}", teacherId.getId(), subjectId.getId());
        teacherDao.addTeacherToSubject(teacherId, subjectId);
        logger.info("ADDED TEACHER BT ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId.getId(), subjectId.getId());
    }

    @Override
    public void addTeacherToSubjects(Teacher teacherId, List<Subject> subjects) {
        requireNonNull(teacherId);
        requireNonNull(subjects);
        requiredTeacherExistence(teacherId.getId());
        subjects.forEach(subject -> requiredSubjectExistence(subject.getId()));
        logger.info("ADDING... TEACHER BY ID - {} TO SUBJECTS {}", teacherId.getId(), subjects.size());
        teacherDao.addTeacherToSubjects(teacherId, subjects);
        logger.info("ADDED TEACHER BY ID - {} TO SUBJECTS {} SUCCESSFULLY", teacherId.getId(), subjects.size());
    }

    @Override
    public List<Subject> findSubjectsByTeacherId(Integer teacherId) {
        requireNonNull(teacherId);
        requiredTeacherExistence(teacherId);
        logger.info("FINDING... SUBJECTS BY TEACHER ID - {}", teacherId);
        List<Subject> subjects = teacherDao.findSubjectsByTeacherId(teacherId);
        logger.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return subjects;
     }

    private void requiredTeacherExistence(Integer teacherId) {
        if (!existsById(teacherId))
            throw new NotFoundException(format("Teacher by id - %d not exists", teacherId));
    }

    private void requiredTeacherExistence(Teacher teacher) {
        if (!existsById(findTeacherByNameSurname(teacher).getId()))
            throw new NotFoundException(format("Teacher - %s not exists", teacher));
    }

    private void requiredSubjectExistence(Integer subjectId) {
        if (!subjectDao.existsById(subjectId))
            throw new NotFoundException(format("Subject by id - %d not exists", subjectId));
    }
}
