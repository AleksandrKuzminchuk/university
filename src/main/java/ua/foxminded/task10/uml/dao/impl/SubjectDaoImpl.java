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
import ua.foxminded.task10.uml.model.Subject;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class SubjectDaoImpl implements SubjectDao {

    private static final Logger logger = LoggerFactory.getLogger(SubjectDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final SubjectRowMapper mapper;

    @Autowired
    public SubjectDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mapper = new SubjectRowMapper();
    }

    @Override
    public Optional<Subject> save(Subject subject) {
        requireNonNull(subject);
        logger.info("SAVING... SUBJECT {}", subject);
        final String SAVE_SUBJECT = "INSERT INTO subjects(subject_name) VALUES (?)";
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
                mapper, id);
        logger.info("FOUND {} BY ID - {} SUCCESSFULLY", result, id);
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
        List<Subject> subjects = jdbcTemplate.query(FIND_ALL, mapper);
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
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, mapper);
        logger.info("DELETED SUBJECT BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Subject subject) {
        requireNonNull(subject);
        logger.info("DELETING {}", subject);
        final String DELETE = "DELETE FROM subjects WHERE subject_name = ?";
        jdbcTemplate.update(DELETE, new Object[]{subject.getName()}, mapper);
        logger.info("DELETED {} SUCCESSFULLY", subject);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL SUBJECTS");
        final String DELETE_ALL = "DELETE FROM subjects";
        jdbcTemplate.update(DELETE_ALL, mapper);
        logger.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        requireNonNull(subjects);
        logger.info("SAVING SUBJECTS {}", subjects.size());
        subjects.forEach(this::save);
        logger.info("SAVED SUBJECTS {} SUCCESSFULLY", subjects.size());
    }

    @Override
    public void updateSubject(Subject subject) {
        requireNonNull(subject);
        logger.info("UPDATING... SUBJECT BY ID - {}", subject.getId());
        final String UPDATE_SUBJECT = "UPDATE subjects SET subject_name = ? WHERE subject_id = ?";
        jdbcTemplate.update(UPDATE_SUBJECT, new Object[]{
                subject.getName(),
                subject.getId()}, mapper);
        logger.info("UPDATED {} SUCCESSFULLY", subject);
    }

    @Override
    public List<Subject> findTeacherSubjects(Integer teacherId) {
        requireNonNull(teacherId);
        logger.info("FINDING SUBJECTS TEACHERS' - {}", teacherId);
        final String FIND_TEACHER_SUBJECTS =
                "SELECT s.subject_id, s.subject_name " +
                "FROM teachers_subjects ts " +
                "JOIN subjects s on s.subject_id = ts.subject_id " +
                "JOIN teachers t on t.teacher_id = ts.teacher_id " +
                "WHERE t.teacher_id = ? " +
                "ORDER BY s.subject_id";
        List<Subject> subjects = jdbcTemplate.query(FIND_TEACHER_SUBJECTS, mapper, teacherId);
        logger.info("FOUND SUBJECTS - {} TEACHERS' BY ID - {}", subjects.size(), teacherId);
        return subjects;
    }
}
