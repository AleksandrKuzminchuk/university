package ua.foxminded.task10.uml.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.model.Classroom;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class ClassroomDaoImpl implements ClassroomDao {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Classroom> mapper;

    public ClassroomDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mapper = new BeanPropertyRowMapper<>(Classroom.class);
    }

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        logger.info("SAVING CLASSROOMS (count = {})", classrooms.size());
        classrooms.forEach(this::save);
        logger.info("SAVED {} SUCCESSFULLY", classrooms.size());
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        requireNonNull(classroom);
        logger.info("UPDATING... CLASSROOM BY ID - {}", classroom.getId());
        final String UPDATE_CLASSROOM = "UPDATE classrooms SET room_number = ? WHERE classroom_id = ?";
        jdbcTemplate.update(UPDATE_CLASSROOM, new Object[]{
                classroom.getNumber(),
                classroom.getId()}, mapper);
        logger.info("UPDATED CLASSROOM - {} SUCCESSFULLY", classroom);
    }

    @Override
    public Optional<Classroom> save(Classroom classroom) {
        requireNonNull(classroom);
        logger.info("SAVING... {}", classroom);
        final String SAVE_CLASSROOM = "INSERT INTO classrooms(room_number) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_CLASSROOM, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, classroom.getNumber());
            return statement;
        }, holder);
        Integer classroomId = requireNonNull(holder.getKey()).intValue();
        classroom.setId(classroomId);
        Optional<Classroom> result = Optional.of(classroom);
        logger.info("SAVED {} SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Optional<Classroom> findById(Integer id) {
        requireNonNull(id);
        logger.info("FINDING... CLASSROOM BY ID - {}", id);
        final String FIND_BY_ID = "SELECT * FROM classrooms WHERE classroom_id = ?";
        Classroom classroom = jdbcTemplate.queryForObject(FIND_BY_ID, mapper, id);
        logger.info("FOUND SUCCESSFULLY {} BY ID - {}", classroom, id);
        return Optional.ofNullable(classroom);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING CLASSROOM EXISTS BY ID - {}", id);
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM classrooms WHERE classroom_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info("CLASSROOM BY ID - {} EXISTS - {}", id, exists);
        return exists;
    }

    @Override
    public List<Classroom> findAll() {
        logger.info("FINDING... ALL CLASSROOMS");
        final String FIND_ALL = "SELECT * FROM classrooms";
        List<Classroom> classrooms = jdbcTemplate.query(FIND_ALL, mapper);
        logger.info("FOUND ALL SUCCESSFULLY {}", classrooms);
        return classrooms;
    }

    @Override
    public Long count() {
        logger.info("FINDING COUNT CLASSROOMS");
        final String COUNT = "SELECT COUNT(*) FROM classrooms";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info("FOUND COUNT({}) CLASSROOMS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETING... CLASSROOM BY ID - {}", id);
        final String DELETE_BY_ID = "DELETE FROM classrooms WHERE classrooms_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, mapper);
        logger.info("DELETED SUCCESSFULLY CLASSROOM BY ID - {}", id);
    }

    @Override
    public void delete(Classroom classroom) {
        requireNonNull(classroom);
        logger.info("DELETING... {}", classroom);
        final String DELETE_CLASSROOM = "DELETE FROM classrooms WHERE room_number = ?";
        jdbcTemplate.update(DELETE_CLASSROOM, new Object[]{classroom.getNumber()}, mapper);
        logger.info("DELETED {} SUCCESSFULLY", classroom);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL CLASSROOMS");
        final String DELETE_ALL = "DELETE FROM classrooms";
        jdbcTemplate.update(DELETE_ALL, mapper);
        logger.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }
}
