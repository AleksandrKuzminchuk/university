package ua.foxminded.task10.uml.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.model.Subject;
import ua.foxminded.task10.uml.model.Teacher;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class TeacherDaoImpl implements TeacherDao {

    private static final Logger logger = Logger.getLogger(TeacherDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Teacher> mapper;

    public TeacherDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mapper = new BeanPropertyRowMapper<>(Teacher.class);
    }

    @Override
    public Optional<Teacher> save(Teacher teacher) {
        requireNonNull(teacher);
        logger.info(format("SAVING %s", teacher));
        final String SAVE_TEACHER = "INSERT INTO teachers (first_name, last_name) VALUES (?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_TEACHER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, teacher.getFirstName());
            statement.setString(2, teacher.getLastName());
            return statement;
        }, holder);
        Integer teacherId = requireNonNull(holder.getKey()).intValue();
        teacher.setId(teacherId);
        Optional<Teacher> result = Optional.of(teacher);
        logger.info(format("%s SAVED SUCCESSFULLY", teacher));
        return result;
    }

    @Override
    public Optional<Teacher> findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING TEACHER BY ID - %d", id));
        final String FIND_TEACHER_BY_ID = "SELECT * FROM teachers WHERE teacher_id = ?";
        Teacher result = jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID, mapper, id);
        logger.info(format("FOUND %s BY ID SUCCESSFULLY", result));
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... TEACHER EXISTS BY ID - %d", id));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM teachers WHERE teacher_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info(format("TEACHER BY ID - %d EXISTS - %s", id, exists));
        return exists;
    }

    @Override
    public List<Teacher> findAll() {
        logger.info("FINDING ALL TEACHERS...");
        final String FIND_ALL = "SELECT * FROM teachers";
        List<Teacher> teachers = jdbcTemplate.query(FIND_ALL, mapper);
        logger.info(format("FOUND ALL TEACHERS: %s", teachers));
        return teachers;
    }

    @Override
    public Long count() {
        logger.info("FIND COUNT ALL TEACHERS...");
        final String COUNT = "SELECT COUNT(*) FROM teachers";
        Long countTeachers = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT(%d) TEACHERS SUCCESSFULLY", countTeachers));
        return countTeachers;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETE TEACHER BY ID - %d", id));
        final String DELETE_BY_ID = "DELETE FROM teachers WHERE teacher_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, mapper);
        logger.info(format("DELETED TEACHER BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Teacher teacher) {
        requireNonNull(teacher);
        logger.info(format("DELETE %s...", teacher));
        final String DELETE_TEACHER = "DELETE FROM teachers WHERE first_name = ? AND last_name = ?";
        jdbcTemplate.update(DELETE_TEACHER, new Object[]{teacher.getFirstName(), teacher.getLastName()}, mapper);
        logger.info(format("DELETED %s SUCCESSFULLY", teacher));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL TEACHERS...");
        final String DELETE_ALL = "DELETE FROM teachers";
        jdbcTemplate.update(DELETE_ALL, mapper);
        logger.info("DELETED ALL TEACHERS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Teacher> teachers) {
        requireNonNull(teachers);
        logger.info(format("SAVING %d TEACHERS", teachers.size()));
        teachers.forEach(this::save);
        logger.info(format("SAVED %d TEACHERS SUCCESSFULLY", teachers.size()));
    }

    @Override
    public void updateTeacher(Teacher teacher) {
        requireNonNull(teacher);
        logger.info(format("UPDATING TEACHER BY ID - %d", teacher.getId()));
        final String UPDATE_TEACHER = "UPDATE teachers SET first_name = ?, last_name = ? WHERE teacher_id = ?";
        jdbcTemplate.update(UPDATE_TEACHER, new Object[]{
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getId()}, mapper);
        logger.info(format("UPDATED TEACHER BY ID - %d SUCCESSFULLY", teacher.getId()));
    }

    @Override
    public void addTeacherToSubject(Integer teacherId, Integer subjectId) {
        requireNonNull(teacherId);
        requireNonNull(subjectId);
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
        requireNonNull(teacherId);
        requireNonNull(subjects);
        logger.info(format("ADDING... TEACHER ID - %d TO SUBJECTS %d", teacherId, subjects.size()));
        subjects.forEach(subject -> addTeacherToSubject(teacherId, subject.getId()));
        logger.info(format("ADDED TEACHER ID - %d TO SUBJECTS %d SUCCESSFULLY", teacherId, subjects.size()));
    }
}
