package ua.foxminded.task10.uml.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.model.place.Classroom;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class ClassroomDaoImpl implements ClassroomDao {

    private static final Logger logger = Logger.getLogger(ClassroomDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public ClassroomDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveAll(List<Classroom> classrooms) {

    }

    @Override
    public Optional<Classroom> findClassroomByName(String classroom) {
        return Optional.empty();
    }

    @Override
    public void updateClassroom(Classroom student) {

    }

    @Override
    public Optional<Classroom> save(Classroom entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Classroom> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<Classroom> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Classroom entity) {

    }

    @Override
    public void deleteAll() {

    }
}
