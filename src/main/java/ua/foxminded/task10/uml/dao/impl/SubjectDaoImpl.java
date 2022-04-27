package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.SubjectDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.curriculums.Subject;
import ua.foxminded.task10.uml.model.people.Teacher;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.String.format;

public class SubjectDaoImpl implements SubjectDao {

    private final static Logger logger = Logger.getLogger(StudentDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public SubjectDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Subject> save(Subject entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... SUBJECT %s", entity));
        final String SAVE_SUBJECT = "INSERT INTO subjects(subject_name) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            return statement;
        }, holder);
        Integer subjectId = holder.getKey().intValue();
        entity.setId(subjectId);
        Optional<Subject> subject = Optional.of(entity);
        logger.info(format("SAVED %s SUCCESSFULLY", subject));
        return subject;
    }

    @Override
    public Optional<Subject> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING SUBJECT BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT * FROM subjects WHERE subject_id = ?";
        Optional<Subject> result = Optional.of(Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                new Object[]{integer}, new BeanPropertyRowMapper<>(Subject.class))))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find subject by ID - %d", integer)));
        logger.info(format("FOUND %s BY ID - %d SUCCESSFULLY", result, integer));
        return result;
    }

    @Override
    public boolean existsById(Integer integer) {
        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<Subject> findAll() {
        logger.info("FINDING... ALL SUBJECTS");
        final String FIND_ALL = "SELECT * FROM subjects";
        List<Subject> subjects = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Subject.class));
        logger.info(format("FOUND ALL SUBJECTS - %d SUCCESSFULLY", subjects.size()));
        return subjects;
    }

    @Override
    public long count() {
        logger.info("FINDING... COUNT SUBJECTS");
        final String COUNT = "SELECT COUNT(*) FROM subjects";
        long count  = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND %d SUBJECTS SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING SUBJECT BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM subjects WHERE subject_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Subject.class));
        logger.info(format("DELETED SUBJECT BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(Subject entity) {
        requiredNonNull(entity);
        logger.info(format("DELETING %s", entity));
        final String DELETE = "DELETE FROM subject WHERE subject_name = ?";
        jdbcTemplate.update(DELETE, new Object[]{entity.getName()}, new BeanPropertyRowMapper<>(Subject.class));
        logger.info(format("DELETED %s SUCCESSFULLY", entity));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL SUBJECTS");
        final String DELETE_ALL = "DELETE FROM subjects";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(Subject.class));
        logger.info("DELETED ALL SUBJECTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        requiredNonNull(subjects);
        logger.info(format("SAVING SUBJECTS %d", subjects.size()));
        subjects.forEach(this::save);
        logger.info(format("SAVED SUBJECTS %d SUCCESSFULLY", subjects.size()));
    }

    @Override
    public void updateSubject(String subjectName, String newSubjectName) {
        requiredNonNull(subjectName);
        requiredNonNull(newSubjectName);
        logger.info(format("UPDATING SUBJECT %s", subjectName));
        final String UPDATE_SUBJECT = "UPDATE subjects SET subject_name = ? WHERE subject_name = ?";
        jdbcTemplate.update(UPDATE_SUBJECT, new Object[]{newSubjectName, subjectName}, new BeanPropertyRowMapper<>(Subject.class));
        logger.info(format("SAVED %s SUCCESSFULLY", newSubjectName));
    }

    @Override
    public List<Subject> findTeacherSubjects(Integer teacherId) {
        requiredNonNull(teacherId);
        logger.info(format("FINDING SUBJECTS TEACHERS' - %s", teacherId));
        final String FIND_TEACHER_SUBJECTS  = "SELECT t.teacher_id, t.first_name, t.last_name, s.subject_id, subject_name " +
                "FROM  teachers_subjects ts JOIN teachers t ON (ts.teacher_id = t.teacher_id) " +
                "JOIN subjects s ON (ts.subject_id = s.subject_id)" +
                "WHERE s.subject_id = ? ORDER BY t.teacher_id";
        List<Subject> subjects = jdbcTemplate.query(FIND_TEACHER_SUBJECTS, new Object[]{teacherId},
                new BeanPropertyRowMapper<>(Subject.class));
        logger.info(format("FOUND SUBJECTS - %d TEACHERS' BY ID - %d", subjects.size(), teacherId));
        return subjects;
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
