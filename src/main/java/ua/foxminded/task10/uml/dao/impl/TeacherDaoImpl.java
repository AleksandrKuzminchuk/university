package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.dao.mapper.SubjectRowMapper;
import ua.foxminded.task10.uml.dao.mapper.TeacherRowMapper;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class TeacherDaoImpl implements TeacherDao {

    private static final Logger logger = LoggerFactory.getLogger(TeacherDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final TeacherRowMapper teacherRowMapper;
    private final SubjectRowMapper subjectRowMapper;

    @Autowired
    public TeacherDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.teacherRowMapper = new TeacherRowMapper();
        this.subjectRowMapper = new SubjectRowMapper();
    }

    @Override
    public Optional<Teacher> save(Teacher teacher) {
        requireNonNull(teacher);
        logger.info("SAVING {}", teacher);
        final String SAVE_TEACHER = "INSERT INTO teachers (first_name, last_name) VALUES (INITCAP(?), INITCAP(?))";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_TEACHER, new String[]{"teacher_id"});
            statement.setString(1, teacher.getFirstName());
            statement.setString(2, teacher.getLastName());
            return statement;
        }, holder);
        Integer teacherId = requireNonNull(holder.getKey()).intValue();
        teacher.setId(teacherId);
        Optional<Teacher> result = Optional.of(teacher);
        logger.info("SAVED {} SUCCESSFULLY", teacher);
        return result;
    }

    @Override
    public Optional<Teacher> findById(Integer id) {
        requireNonNull(id);
        logger.info("FINDING TEACHER BY ID - {}", id);
        final String FIND_TEACHER_BY_ID = "SELECT * FROM teachers WHERE teacher_id = ?";
        Teacher result = jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID, teacherRowMapper, id);
        logger.info("FOUND {} BY ID SUCCESSFULLY", result);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Teacher> findTeacherByNameSurname(Teacher teacher) {
        requireNonNull(teacher);
        logger.info("FIND TEACHER {}", teacher);
        final String FIND_BY_NAME_SURNAME = "SELECT * FROM teachers WHERE first_name = INITCAP(?) AND last_name = INITCAP(?)";
        Teacher result = jdbcTemplate.queryForObject(FIND_BY_NAME_SURNAME, teacherRowMapper, teacher.getFirstName(), teacher.getLastName());
        logger.info("FOUND TEACHER {}", result);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... TEACHER EXISTS BY ID - {}", id);
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM teachers WHERE teacher_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info("TEACHER BY ID - {} EXISTS - {}", id, exists);
        return exists;
    }

    @Override
    public List<Teacher> findAll() {
        logger.info("FINDING ALL TEACHERS...");
        final String FIND_ALL = "SELECT * FROM teachers ORDER BY first_name, last_name";
        List<Teacher> teachers = jdbcTemplate.query(FIND_ALL, teacherRowMapper);
        logger.info("FOUND ALL TEACHERS: {}", teachers.size());
        return teachers;
    }

    @Override
    public Long count() {
        logger.info("FIND COUNT ALL TEACHERS...");
        final String COUNT = "SELECT COUNT(*) FROM teachers";
        Long countTeachers = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info("FOUND COUNT({}) TEACHERS SUCCESSFULLY", countTeachers);
        return countTeachers;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETE TEACHER BY ID - {}", id);
        final String DELETE_BY_ID = "DELETE FROM teachers WHERE teacher_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, id);
        logger.info("DELETED TEACHER BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Teacher teacher) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL TEACHERS...");
        final String DELETE_ALL = "DELETE FROM teachers";
        jdbcTemplate.update(DELETE_ALL);
        logger.info("DELETED ALL TEACHERS SUCCESSFULLY");
    }

    @Override
    public void deleteTheTeacherSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        logger.info("DELETE THE TEACHERS' BY ID - {} SUBJECT BY ID - {}", teacherId, subjectId);
        final String DELETE_THE_TEACHERS_GROUPS = "DELETE FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?";
        jdbcTemplate.update(DELETE_THE_TEACHERS_GROUPS, teacherId, subjectId);
        logger.info("DELETED THE TEACHERS' BY ID - {} SUBJECTS BY ID - {} SUCCESSFULLY", teacherId, subjectId);
    }

    @Override
    public void saveAll(List<Teacher> teachers) {
        requireNonNull(teachers);
        logger.info("SAVING {} TEACHERS", teachers.size());
        teachers.forEach(this::save);
        logger.info("SAVED {} TEACHERS SUCCESSFULLY", teachers.size());
    }

    @Override
    public void updateTeacher(Integer teacherId, Teacher teacher) {
        requireNonNull(teacher);
        requireNonNull(teacherId);
        logger.info("UPDATING TEACHER BY ID - {}", teacherId);
        final String UPDATE_TEACHER = "UPDATE teachers SET first_name = INITCAP(?), last_name = INITCAP(?) WHERE teacher_id = ?";
        jdbcTemplate.update(UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacherId);
        logger.info("UPDATED TEACHER BY ID - {} SUCCESSFULLY", teacherId);
    }

    @Override
    public void updateTheTeacherSubject(Integer teacherId, Integer oldSubjectId, Integer newSubjectId) {
        requireNonNull(teacherId);
        requireNonNull(oldSubjectId);
        requireNonNull(newSubjectId);
        logger.info("UPDATE THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {}", teacherId, oldSubjectId, newSubjectId);
        final String UPDATE_THE_TEACHER_SUBJECT = "UPDATE teachers_subjects SET subject_id = ? WHERE teacher_id = ? AND subject_id = ?";
        jdbcTemplate.update(UPDATE_THE_TEACHER_SUBJECT, newSubjectId, teacherId, oldSubjectId);
        logger.info("UPDATED THE TEACHERS' BY ID - {} SUBJECT BY ID - {} TO SUBJECT BY ID - {} SUCCESSFULLY", teacherId, oldSubjectId, newSubjectId);
    }

    @Override
    public void addTeacherToSubject(Teacher teacherId, Subject subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
        logger.info("ADDING... TEACHER ID - {} TO SUBJECT ID - {}", teacherId.getId(), subjectId.getId());
        final String ADD_TEACHER_TO_SUBJECT = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(ADD_TEACHER_TO_SUBJECT);
            statement.setInt(1, teacherId.getId());
            statement.setInt(2, subjectId.getId());
            logger.info("ADDED TEACHER ID - {} TO SUBJECT ID - {} SUCCESSFULLY", teacherId.getId(), subjectId.getId());
            return statement;
        });
    }

    @Override
    public void addTeacherToSubjects(Teacher teacherId, List<Subject> subjects) {
        requireNonNull(teacherId);
        requireNonNull(subjects);
        logger.info("ADDING... TEACHER ID - {} TO SUBJECTS {}", teacherId, subjects.size());
        subjects.forEach(subject -> addTeacherToSubject(teacherId, subject));
        logger.info("ADDED TEACHER ID - {} TO SUBJECTS {} SUCCESSFULLY", teacherId, subjects.size());
    }

    @Override
    public List<Subject> findSubjectsByTeacherId(Integer teacherId) {
        requireNonNull(teacherId);
        logger.info("FIND SUBJECTS BY TEACHER ID - {}", teacherId);
        final String FIND_SUBJECTS_BY_TEACHER_ID = "SELECT s.subject_id ,s.subject_name " +
                "FROM teachers_subjects ts " +
                "FULL OUTER JOIN subjects s ON ts.subject_id = s.subject_id " +
                "FULL OUTER JOIN teachers t ON t.teacher_id = ts.teacher_id " +
                "WHERE ts.teacher_id = ? " +
                "ORDER BY subject_name";
        List<Subject> subjects = jdbcTemplate.query(FIND_SUBJECTS_BY_TEACHER_ID, subjectRowMapper, teacherId);
        logger.info("FOUND SUBJECTS {} BY TEACHER ID - {} SUCCESSFULLY", subjects.size(), teacherId);
        return subjects;
    }
}
