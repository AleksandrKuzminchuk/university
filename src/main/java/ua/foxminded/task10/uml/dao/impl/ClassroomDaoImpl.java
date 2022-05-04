package ua.foxminded.task10.uml.dao.impl;

import org.apache.log4j.Logger;
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
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class ClassroomDaoImpl implements ClassroomDao {

    private static final Logger logger = Logger.getLogger(ClassroomDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Classroom> mapper;

    public ClassroomDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mapper = new BeanPropertyRowMapper<>(Classroom.class);
    }

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        logger.info(format("SAVING CLASSROOMS (count = %d)", classrooms.size()));
        classrooms.forEach(this::save);
        logger.info(format("SAVED %d SUCCESSFULLY", classrooms.size()));
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        requireNonNull(classroom);
        logger.info(format("UPDATING... CLASSROOM BY ID - %d", classroom.getId()));
        final String UPDATE_CLASSROOM = "UPDATE classrooms SET room_number = ? WHERE classroom_id = ?";
        jdbcTemplate.update(UPDATE_CLASSROOM, new Object[]{
                classroom.getNumber(),
                classroom.getId()}, mapper);
        logger.info(format("UPDATED CLASSROOM - %s SUCCESSFULLY", classroom));
    }

    @Override
    public Optional<Classroom> save(Classroom classroom) {
        requireNonNull(classroom);
        logger.info(format("SAVING... %s", classroom));
        final String SAVE_CLASSROOM = "INSERT INTO classrooms(room_number) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_CLASSROOM, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, classroom.getNumber());
            return statement;
        }, holder);
        Integer classroomId = Objects.requireNonNull(holder.getKey()).intValue();
        classroom.setId(classroomId);
        Optional<Classroom> result = Optional.of(classroom);
        logger.info(format("SAVED %s SUCCESSFULLY", result));
        return result;
    }

    @Override
    public Optional<Classroom> findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... CLASSROOM BY ID - %d", id));
        final String FIND_BY_ID = "SELECT * FROM classrooms WHERE classroom_id = ?";
        Classroom classroom = jdbcTemplate.queryForObject(FIND_BY_ID, mapper, id);
        logger.info(format("FOUND SUCCESSFULLY %s BY ID - %d", classroom, id));
        return Optional.ofNullable(classroom);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING CLASSROOM EXISTS BY ID - %d", id));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM classrooms WHERE classroom_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info(format("CLASSROOM BY ID - %d EXISTS - %s", id, exists));
        return exists;
    }

    @Override
    public List<Classroom> findAll() {
        logger.info("FINDING... ALL CLASSROOMS");
        final String FIND_ALL = "SELECT * FROM classrooms";
        List<Classroom> classrooms = jdbcTemplate.query(FIND_ALL, mapper);
        logger.info(format("FOUND ALL SUCCESSFULLY %s", classrooms));
        return classrooms;
    }

    @Override
    public Long count() {
        logger.info("FINDING COUNT CLASSROOMS");
        final String COUNT = "SELECT COUNT(*) FROM classrooms";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT(%d) CLASSROOMS SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING... CLASSROOM BY ID - %d", id));
        final String DELETE_BY_ID = "DELETE FROM classrooms WHERE classrooms_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, mapper);
        logger.info(format("DELETED SUCCESSFULLY CLASSROOM BY ID - %d", id));
    }

    @Override
    public void delete(Classroom classroom) {
        requireNonNull(classroom);
        logger.info(format("DELETING... %s", classroom));
        final String DELETE_CLASSROOM = "DELETE FROM classrooms WHERE room_number = ?";
        jdbcTemplate.update(DELETE_CLASSROOM, new Object[]{classroom.getNumber()}, mapper);
        logger.info(format("DELETED %s SUCCESSFULLY", classroom));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL CLASSROOMS");
        final String DELETE_ALL = "DELETE FROM classrooms";
        jdbcTemplate.update(DELETE_ALL, mapper);
        logger.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }
}
