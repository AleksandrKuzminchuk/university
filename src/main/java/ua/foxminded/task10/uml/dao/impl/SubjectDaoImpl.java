package ua.foxminded.task10.uml.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.SubjectDao;
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
public class SubjectDaoImpl implements SubjectDao {

    private static final Logger logger = LoggerFactory.getLogger(SubjectDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final SubjectRowMapper subjectRowMapper;
    private final TeacherRowMapper teacherRowMapper;

    @Autowired
    public SubjectDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.subjectRowMapper = new SubjectRowMapper();
        this.teacherRowMapper = new TeacherRowMapper();
    }

    @Override
    public Optional<Subject> save(Subject subject) {
        requireNonNull(subject);
        logger.info("SAVING... SUBJECT {}", subject);
        final String SAVE_SUBJECT = "INSERT INTO subjects(subject_name) VALUES (UPPER(?))";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_SUBJECT, new String[]{"subject_id"});
            statement.setString(1, subject.getName());
            return statement;
        }, holder);
        Integer subjectId = requireNonNull(holder.getKey()).intValue();
        subject.setId(subjectId);
        Optional<Subject> result = Optional.of(subject);
        logger.info("SAVED {} SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Optional<Subject> findById(Integer id) {
        requireNonNull(id);
        logger.info("FINDING SUBJECT BY ID - {}", id);
        final String FIND_BY_ID = "SELECT * FROM subjects WHERE subject_id = ?";
        Subject result = jdbcTemplate.queryForObject(FIND_BY_ID,
                subjectRowMapper, id);
        logger.info("FOUND {} BY ID - {} SUCCESSFULLY", result, id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Subject> findSubjectByName(Subject subject) {
        requireNonNull(subject);
        logger.info("FIND SUBJECT BY NAME {}", subject.getName());
        final String FIND_BY_NAME = "SELECT * FROM subjects WHERE subject_name = UPPER(?)";
        Subject result = jdbcTemplate.queryForObject(FIND_BY_NAME, subjectRowMapper, subject.getName());
        logger.info("FOUND SUBJECT BY NAME {}", subject.getName());
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... SUBJECT EXISTS BY ID - {}", id);
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM subjects WHERE subject_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info("SUBJECT BY ID - {} EXISTS - {}", id, exists);
        return exists;
    }

    @Override
    public List<Subject> findAll() {
        logger.info("FINDING... ALL SUBJECTS");
        final String FIND_ALL = "SELECT * FROM subjects";
        List<Subject> subjects = jdbcTemplate.query(FIND_ALL, subjectRowMapper);
        logger.info("FOUND ALL SUBJECTS - {} SUCCESSFULLY", subjects.size());
        return subjects;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT SUBJECTS");
        final String COUNT = "SELECT COUNT(*) FROM subjects";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info("FOUND COUNT({}) SUBJECTS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETING SUBJECT BY ID - {}", id);
        final String DELETE_BY_ID = "DELETE FROM subjects WHERE subject_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, id);
        logger.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Subject subject) {
        requireNonNull(subject);
        logger.info("DELETING {}", subject);
        final String DELETE = "DELETE FROM subjects WHERE subject_name = UPPER(?)";
        jdbcTemplate.update(DELETE, subject.getName());
        logger.info("DELETED {} SUCCESSFULLY", subject);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL SUBJECTS");
        final String DELETE_ALL = "DELETE FROM subjects";
        jdbcTemplate.update(DELETE_ALL);
        logger.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void deleteTheSubjectTeacher(Integer subjectId, Integer teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        logger.info("DELETE THE SUBJECTS' BY ID - {} TEACHER BY ID - {}", subjectId, teacherId);
        final String DELETE_THE_SUBJECT_TEACHER = "DELETE FROM teachers_subjects WHERE subject_id = ? AND teacher_id = ?";
        jdbcTemplate.update(DELETE_THE_SUBJECT_TEACHER, subjectId, teacherId);
        logger.info("DELETED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} SUCCESSFULLY", subjectId, teacherId);
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        requireNonNull(subjects);
        logger.info("SAVING SUBJECTS {}", subjects.size());
        subjects.forEach(this::save);
        logger.info("SAVED SUBJECTS {} SUCCESSFULLY", subjects.size());
    }

    @Override
    public void updateSubject(Integer subjectId, Subject subject) {
        requireNonNull(subject);
        requireNonNull(subjectId);
        logger.info("UPDATING... SUBJECT BY ID - {}", subjectId);
        final String UPDATE_SUBJECT = "UPDATE subjects SET subject_name = UPPER(?) WHERE subject_id = ?";
        jdbcTemplate.update(UPDATE_SUBJECT, subject.getName(),
                subjectId);
        logger.info("UPDATED {} SUCCESSFULLY", subject);
    }

    @Override
    public void updateTheSubjectTeacher(Integer subjectId, Integer oldTeacherId, Integer newTeacherId) {
        requireNonNull(subjectId);
        requireNonNull(oldTeacherId);
        requireNonNull(newTeacherId);
        logger.info("UPDATE THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {}", subjectId, oldTeacherId, newTeacherId);
        final String UPDATE_THE_SUBJECT_TEACHER = "UPDATE teachers_subjects SET teacher_id = ? WHERE subject_id = ? AND teacher_id = ?";
        jdbcTemplate.update(UPDATE_THE_SUBJECT_TEACHER, newTeacherId, subjectId, oldTeacherId);
        logger.info("UPDATED THE SUBJECTS' BY ID - {} TEACHER BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId, oldTeacherId, newTeacherId);
    }

    @Override
    public List<Teacher> findTeachersBySubject(Integer subjectId) {
        requireNonNull(subjectId);
        logger.info("FINDING TEACHERS BY SUBJECT ID - {}", subjectId);
        final String FIND_TEACHERS_BY_SUBJECT = "SELECT t.teacher_id, t.first_name, t.last_name " +
                "FROM teachers_subjects ts " +
                "FULL OUTER JOIN teachers t on t.teacher_id = ts.teacher_id " +
                "FULL OUTER JOIN subjects s on s.subject_id = ts.subject_id " +
                "WHERE ts.subject_id = ? " +
                "ORDER BY t.first_name";
        List<Teacher> teachers = jdbcTemplate.query(FIND_TEACHERS_BY_SUBJECT, teacherRowMapper, subjectId);
        logger.info("FOUND {} TEACHERS BY SUBJECT ID - {}", teachers.size(), subjectId);
        return teachers;
    }

    @Override
    public void addSubjectToTeacher(Subject subjectId, Teacher teacherId) {
        requireNonNull(subjectId);
        requireNonNull(teacherId);
        logger.info("ADD SUBJECT BY ID - {} TO TEACHER BY ID - {}", subjectId.getTeacher(), teacherId.getId());
        final String ADD_SUBJECT_TO_TEACHER = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(ADD_SUBJECT_TO_TEACHER);
            statement.setInt(1, teacherId.getId());
            statement.setInt(2, subjectId.getId());
            logger.info("ADDED SUBJECT BY ID - {} TO TEACHER BY ID - {} SUCCESSFULLY", subjectId.getName(), teacherId.getId());
            return statement;
        });
    }

    @Override
    public void addSubjectToTeachers(Subject subjectId, List<Teacher> teachers) {
        requireNonNull(subjectId);
        requireNonNull(teachers);
        logger.info("ADD SUBJECT BY ID - {} TO TEACHERS - {}", subjectId.getId(), teachers.size());
        teachers.forEach(teacher -> addSubjectToTeacher(subjectId, teacher));
        logger.info("ADDED SUBJECT BY ID - {} TO TEACHERS - {} SUCCESSFULLY", subjectId.getId(), teachers.size());
    }
}
