package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.curriculums.Subject;
import ua.foxminded.task10.uml.model.people.Teacher;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    private final static Logger logger = Logger.getLogger(Teacher.class);

    public TeacherDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Teacher> save(Teacher entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING %s", entity));
        final String SAVE_TEACHER = "INSERT INTO teachers (first_name, last_name) VALUES (?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(SAVE_TEACHER, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                return statement;
            }, holder);
            Integer teacherId = holder.getKey().intValue();
            entity.setId(teacherId);
            Optional<Teacher> result = Optional.of(entity);
            logger.info(format("%s SAVED SUCCESSFULLY", entity));
            return result;
    }

    @Override
    public Optional<Teacher> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING TEACHER BY ID - %d", integer));
        final String FIND_TEACHER_BY_ID = "SELECT * FROM teachers WHERE teacher_id = ?";
        Optional<Teacher> teacher =  Optional.of(Optional.ofNullable(jdbcTemplate
                .queryForObject(FIND_TEACHER_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Teacher.class)))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find teacher by id - %d", integer))));
        logger.info(format("FOUND %s BY ID SUCCESSFULLY", teacher));
        return teacher;
    }

    @Override
    public boolean existsById(Integer integer) {
        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<Teacher> findAll() {
        logger.info("FINDING ALL TEACHERS...");
        final String FIND_ALL = "SELECT * FROM teachers";
        List<Teacher> teachers = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Teacher.class));
            logger.info(format("FOUND ALL TEACHERS: %s", teachers));
            return teachers;
     }

    @Override
    public long count() {
        logger.info("FIND COUNT ALL TEACHERS...");
        final String COUNT = "SELECT COUNT(*) FROM teachers";
        long countTeachers = jdbcTemplate.queryForObject(COUNT, Long.class);
            logger.info(format("FOUND COUNT ALL TEACHERS - %d", countTeachers));
            return countTeachers;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETE TEACHER BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM teachers WHERE teacher_id = ?";
            jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Teacher.class));
            logger.info(format("DELETED TEACHER BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(Teacher entity) {
        requiredNonNull(entity);
        logger.info(format("DELETE %s...", entity));
        final String DELETE_TEACHER = "DELETE FROM teachers WHERE first_name = ? AND last_name = ?";
            jdbcTemplate.update(DELETE_TEACHER, new Object[]{entity.getFirstName(), entity.getLastName()}, new BeanPropertyRowMapper<>(Teacher.class));
            logger.info(format("DELETED %s SUCCESSFULLY", entity));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL TEACHERS...");
        final String DELETE_ALL = "DELETE FROM teachers";
            jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(Teacher.class));
            logger.info("DELETED ALL TEACHERS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Teacher> teachers) {
        requiredNonNull(teachers);
        logger.info(format("SAVING %d TEACHERS", teachers.size()));
            teachers.forEach(this::save);
            logger.info(format("SAVED %d TEACHERS SUCCESSFULLY", teachers.size()));
    }

    @Override
    public void updateTeacher(String firstName, String lastName, Teacher teacher) {
        requiredNonNull(teacher);
        requiredNonNull(firstName);
        requiredNonNull(lastName);
        logger.info(format("UPDATING TEACHER %s %s", firstName, lastName));
        final String UPDATE_TEACHER = "UPDATE teachers SET first_name = ?, last_name = ? " +
                "WHERE first_name = ? AND last_name = ?";
            jdbcTemplate.update(UPDATE_TEACHER, new Object[]{teacher.getFirstName()
            , teacher.getLastName(), firstName, lastName}, new BeanPropertyRowMapper<>(Teacher.class));
            logger.info(format("UPDATED %s", teacher));
    }

    @Override
    public void addTeacherToSubject(Integer teacherId, Integer subjectId) {
        requiredNonNull(teacherId);
        requiredNonNull(subjectId);
        logger.info(format("ADDING... TEACHER ID - %d TO SUBJECT ID - %d", teacherId, subjectId));
        final String ADD_TEACHER_TO_SUBJECT = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(ADD_TEACHER_TO_SUBJECT);
            statement.setInt(1, teacherId);
            statement.setInt(2, subjectId);
            statement.executeUpdate();
            logger.info(format("ADDED TEACHER ID - %d TO SUBJECT ID - %d SUCCESSFULLY", teacherId, subjectId));
            return statement;
        });
    }

    @Override
    public void addTeacherToSubjects(Integer teacherId, List<Subject> subjects) {
        requiredNonNull(teacherId);
        requiredNonNull(subjects);
        logger.info(format("ADDING... TEACHER ID - %d TO SUBJECTS %d", teacherId, subjects.size()));
        subjects.forEach(subject -> addTeacherToSubject(teacherId, subject.getId()));
        logger.info(format("ADDED TEACHER ID - %d TO SUBJECTS %d SUCCESSFULLY", teacherId, subjects.size()));
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
