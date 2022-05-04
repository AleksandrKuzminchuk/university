package ua.foxminded.task10.uml.dao.impl;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.SubjectDao;
import ua.foxminded.task10.uml.model.Subject;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class SubjectDaoImpl implements SubjectDao {

    private static final Logger logger = Logger.getLogger(SubjectDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Subject> mapper;

    public SubjectDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mapper = new BeanPropertyRowMapper<>(Subject.class);
    }

    @Override
    public Optional<Subject> save(Subject subject) {
        requireNonNull(subject);
        logger.info(format("SAVING... SUBJECT %s", subject));
        final String SAVE_SUBJECT = "INSERT INTO subjects(subject_name) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, subject.getName());
            return statement;
        }, holder);
        Integer subjectId = requireNonNull(holder.getKey()).intValue();
        subject.setId(subjectId);
        Optional<Subject> result = Optional.of(subject);
        logger.info(format("SAVED %s SUCCESSFULLY", result));
        return result;
    }

    @Override
    public Optional<Subject> findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING SUBJECT BY ID - %d", id));
        final String FIND_BY_ID = "SELECT * FROM subjects WHERE subject_id = ?";
        Subject result = jdbcTemplate.queryForObject(FIND_BY_ID,
                mapper, id);
        logger.info(format("FOUND %s BY ID - %d SUCCESSFULLY", result, id));
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... SUBJECT EXISTS BY ID - %d", id));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM subjects WHERE subject_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info(format("SUBJECT BY ID - %d EXISTS - %s", id, exists));
        return exists;
    }

    @Override
    public List<Subject> findAll() {
        logger.info("FINDING... ALL SUBJECTS");
        final String FIND_ALL = "SELECT * FROM subjects";
        List<Subject> subjects = jdbcTemplate.query(FIND_ALL, mapper);
        logger.info(format("FOUND ALL SUBJECTS - %d SUCCESSFULLY", subjects.size()));
        return subjects;
    }

    @Override
    public Long count() {
        logger.info("FINDING... COUNT SUBJECTS");
        final String COUNT = "SELECT COUNT(*) FROM subjects";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class); 
        logger.info(format("FOUND COUNT(%d) SUBJECTS SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING SUBJECT BY ID - %d", id));
        final String DELETE_BY_ID = "DELETE FROM subjects WHERE subject_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, mapper);
        logger.info(format("DELETED SUBJECT BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Subject subject) {
        requireNonNull(subject);
        logger.info(format("DELETING %s", subject));
        final String DELETE = "DELETE FROM subject WHERE subject_name = ?";
        jdbcTemplate.update(DELETE, new Object[]{subject.getName()}, mapper);
        logger.info(format("DELETED %s SUCCESSFULLY", subject));
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
        logger.info(format("SAVING SUBJECTS %d", subjects.size()));
        subjects.forEach(this::save);
        logger.info(format("SAVED SUBJECTS %d SUCCESSFULLY", subjects.size()));
    }

    @Override
    public void updateSubject(Subject subject) {
        requireNonNull(subject);
        logger.info(format("UPDATING... SUBJECT BY ID - %d", subject.getId()));
        final String UPDATE_SUBJECT = "UPDATE subjects SET subject_name = ? WHERE subject_id = ?";
        jdbcTemplate.update(UPDATE_SUBJECT, new Object[]{
                subject.getName(), 
                subject.getId()}, mapper);
        logger.info(format("UPDATED %s SUCCESSFULLY", subject));
    }

    @Override
    public List<Subject> findTeacherSubjects(Integer teacherId) {
         requireNonNull(teacherId);
        logger.info(format("FINDING SUBJECTS TEACHERS' - %s", teacherId));
        final String FIND_TEACHER_SUBJECTS = "SELECT " +
                "t.teacher_id, " +
                "t.first_name, " +
                "t.last_name, " +
                "s.subject_id, " +
                "subject_name " +
                "FROM  teachers_subjects ts " +
                "JOIN teachers t ON (ts.teacher_id = t.teacher_id) " +
                "JOIN subjects s ON (ts.subject_id = s.subject_id)" +
                "WHERE s.subject_id = ? " +
                "ORDER BY t.teacher_id";
        List<Subject> subjects = jdbcTemplate.query(FIND_TEACHER_SUBJECTS,                mapper, teacherId);
        logger.info(format("FOUND SUBJECTS - %d TEACHERS' BY ID - %d", subjects.size(), teacherId));
        return subjects;
    }
}
