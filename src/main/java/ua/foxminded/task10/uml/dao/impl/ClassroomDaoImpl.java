package ua.foxminded.task10.uml.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.dao.mapper.ClassroomRowMapper;
import ua.foxminded.task10.uml.model.Classroom;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class ClassroomDaoImpl implements ClassroomDao {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final ClassroomRowMapper classroomRowMapper;

    @Autowired
    public ClassroomDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.classroomRowMapper = new ClassroomRowMapper();
    }

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        logger.info("SAVING CLASSROOMS (count = {})", classrooms.size());
        classrooms.forEach(this::save);
        logger.info("SAVED {} SUCCESSFULLY", classrooms.size());
    }

    @Override
    public void updateClassroom(Integer classroomId, Classroom classroom) {
        requireNonNull(classroom);
        requireNonNull(classroomId);
        logger.info("UPDATING... CLASSROOM BY ID - {}", classroomId);
        final String UPDATE_CLASSROOM = "UPDATE classrooms SET room_number = ? WHERE classroom_id = ?";
        jdbcTemplate.update(UPDATE_CLASSROOM, classroom.getNumber(), classroomId);
        logger.info("UPDATED CLASSROOM - {} SUCCESSFULLY", classroom);
    }

    @Override
    public Optional<Classroom> save(Classroom classroom) {
        requireNonNull(classroom);
        logger.info("SAVING... {}", classroom);
        final String SAVE_CLASSROOM = "INSERT INTO classrooms(room_number) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_CLASSROOM, new String[]{"classroom_id"});
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
        Classroom classroom = jdbcTemplate.queryForObject(FIND_BY_ID, classroomRowMapper, id);
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
        List<Classroom> classrooms = jdbcTemplate.query(FIND_ALL, classroomRowMapper);
        logger.info("FOUND ALL SUCCESSFULLY {}", classrooms.size());
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
        final String DELETE_BY_ID = "DELETE FROM classrooms WHERE classroom_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, id);
        logger.info("DELETED SUCCESSFULLY CLASSROOM BY ID - {}", id);
    }

    @Override
    public void delete(Classroom classroom) {
        requireNonNull(classroom);
        logger.info("DELETING... {}", classroom);
        final String DELETE_CLASSROOM = "DELETE FROM classrooms WHERE room_number = ?";
        jdbcTemplate.update(DELETE_CLASSROOM, classroom.getNumber());
        logger.info("DELETED {} SUCCESSFULLY", classroom);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL CLASSROOMS");
        final String DELETE_ALL = "DELETE FROM classrooms";
        jdbcTemplate.update(DELETE_ALL);
        logger.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }

    @Override
    public Optional<Classroom> findClassroomByNumber(Integer classroomNumber) {
        requireNonNull(classroomNumber);
        logger.info("FIND CLASSROOM BY NUMBER - {}", classroomNumber);
        final String FIND_BY_NUMBER = "SELECT * FROM classrooms WHERE room_number = ?";
        Classroom classroom = jdbcTemplate.queryForObject(FIND_BY_NUMBER, classroomRowMapper, classroomNumber);
        logger.info("FOUND CLASSROOM BY NUMBER - {} SUCCESSFULLY", classroomNumber);
        return Optional.ofNullable(classroom);
    }
}
