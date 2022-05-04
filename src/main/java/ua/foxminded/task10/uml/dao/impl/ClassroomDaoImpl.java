package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.place.Classroom;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class ClassroomDaoImpl implements ClassroomDao {

    private static final Logger logger = Logger.getLogger(ClassroomDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public ClassroomDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requiredNonNull(classrooms);
        logger.info(format("SAVING... CLASSROOMS %d", classrooms.size()));
            classrooms.forEach(this::save);
            logger.info(format("SAVED %d SUCCESSFULLY", classrooms.size()));
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        requiredNonNull(classroom);
        logger.info(format("UPDATING... CLASSROOM BY ID - %d", classroom.getId()));
        final String UPDATE_CLASSROOM = "UPDATE classrooms SET room_number = ? WHERE classroom_id = ?";
            jdbcTemplate.update(UPDATE_CLASSROOM, new Object[]{classroom.getNumber(), classroom.getId()}, new BeanPropertyRowMapper<>(Classroom.class));
            logger.info(format("UPDATED CLASSROOM - %s SUCCESSFULLY", classroom));
    }

    @Override
    public Optional<Classroom> save(Classroom entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... %s", entity));
        final String SAVE_CLASSROOM = "INSERT INTO classrooms(room_number) VALUES (?)";
        Optional<Classroom> classroom;
        KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(SAVE_CLASSROOM, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, entity.getNumber());
                return statement;
            }, holder);
            Integer classroomId = holder.getKey().intValue();
            entity.setId(classroomId);
            classroom = Optional.of(entity);
            logger.info(format("SAVED %s SUCCESSFULLY", entity));
            return classroom;
    }

    @Override
    public Optional<Classroom> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING... CLASSROOM BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT * FROM classrooms WHERE classroom_id = ?";
        Classroom classroom = Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Classroom.class)
            )).orElseThrow(() -> new IllegalArgumentException(format("Can't find classroom by id - %d", integer)));
        logger.info(format("FOUND SUCCESSFULLY %s BY ID - %d", classroom, integer));
        return Optional.of(classroom);
    }

    @Override
    public boolean existsById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("CHECKING CLASSROOM EXISTS BY ID - %d", integer));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM classrooms WHERE classroom_id = ?";
        boolean result = false;

        long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, new Object[]{integer}, Long.class);
        if (count > 0){
            result = true;
            logger.info(format("CHECKED CLASSROOM BY ID - %d EXISTS", integer));
        }
        return result;
    }

    @Override
    public List<Classroom> findAll() {
        logger.info("FINDING... ALL CLASSROOMS");
        final String FIND_ALL = "SELECT * FROM classrooms";
        List<Classroom> classrooms = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Classroom.class));
            logger.info(format("FOUND ALL SUCCESSFULLY %s", classrooms));
            return classrooms;
    }

    @Override
    public long count() {
        logger.info("FINDING COUNT CLASSROOMS");
        final String COUNT = "SELECT COUNT(*) FROM classrooms";
        long count = jdbcTemplate.queryForObject(COUNT, Long.class);
            logger.info(format("FOUND COUNT(%d) CLASSROOMS SUCCESSFULLY", count));
            return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING... CLASSROOM BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM classrooms WHERE classrooms_id = ?";
            jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Classroom.class));
            logger.info(format("DELETED SUCCESSFULLY CLASSROOM BY ID - %d", integer));
    }

    @Override
    public void delete(Classroom entity) {
        requiredNonNull(entity);
        logger.info(format("DELETING... %s", entity));
        final String DELETE_CLASSROOM = "DELETE FROM classrooms WHERE room_number = ?";
            jdbcTemplate.update(DELETE_CLASSROOM, new Object[]{entity.getNumber()}, new BeanPropertyRowMapper<>(Classroom.class));
            logger.info(format("DELETED %s SUCCESSFULLY", entity));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL CLASSROOMS");
        final String DELETE_ALL = "DELETE FROM classrooms";
            jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(Classroom.class));
            logger.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
