package ua.foxminded.task10.uml.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.task10.uml.dao.SubjectDao;
import ua.foxminded.task10.uml.model.curriculums.Subject;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class SubjectDaoImpl implements SubjectDao {

    private final static Logger logger = Logger.getLogger(StudentDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public SubjectDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Subject> save(Subject entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Subject> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<Subject> findAll() {
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
    public void delete(Subject entity) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void saveAll(List<Subject> students) {

    }

    @Override
    public Optional<Subject> findSubjectByName(String subjectName) {
        return Optional.empty();
    }

    @Override
    public void updateSubject(Subject subject) {

    }
}
