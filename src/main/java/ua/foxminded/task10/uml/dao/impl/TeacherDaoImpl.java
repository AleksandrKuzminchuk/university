package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
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
        final String SAVE_TEACHER = "INSERT INTO teacher (first_name, last_name) VALUES (?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con ->  {
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
        final String FIND_TEACHER_BY_ID = "SELECT * FROM teacher WHERE teacher_id = ?";
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
        final String FIND_ALL = "SELECT * FROM teacher";
        List<Teacher> teachers = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Teacher.class));
        logger.info(format("FOUND ALL TEACHERS: %s", teachers));
        return teachers;
     }

    @Override
    public long count() {
        logger.info("FIND COUNT ALL TEACHERS...");
        final String COUNT = "SELECT COUNT(*) FROM teacher";
        long countTeachers = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUND ALL TEACHERS - %d", countTeachers));
        return countTeachers;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETE TEACHER BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM teacher WHERE teacher_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Teacher.class));
        logger.info(format("DELETED TEACHER BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(Teacher entity) {
        requiredNonNull(entity);
        logger.info(format("DELETE %s...", entity));
        final String DELETE_TEACHER = "DELETE FROM teacher WHERE first_name = ? AND last_name = ?";
        jdbcTemplate.update(DELETE_TEACHER, new Object[]{entity.getFirstName(), entity.getLastName()}, new BeanPropertyRowMapper<>(Teacher.class));
        logger.info(format("DELETED %s SUCCESSFULLY", entity));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL TEACHERS...");
        final String DELETE_ALL = "DELETE FROM teacher";
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
    public void updateTeacher(Teacher teacher) {
        requiredNonNull(teacher);
        logger.info(format("UPDATING %s...", teacher));
        final String UPDATE_TEACHER = "UPDATE teacher SET first_name = ?, last_name = ? WHERE teacher_id = ?";
        jdbcTemplate.update(UPDATE_TEACHER, teacher.getId());
        logger.info(format("UPDATED %s", teacher));
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
