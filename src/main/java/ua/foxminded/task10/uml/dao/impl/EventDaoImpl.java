package ua.foxminded.task10.uml.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.task10.uml.dao.EventDao;
import ua.foxminded.task10.uml.model.curriculums.Event;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class EventDaoImpl implements EventDao {

    private final static Logger logger = Logger.getLogger(EventDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public EventDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Event> save(Event entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Event> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<Event> findAll() {
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
    public void saveAll(List<Event> events) {

    }

    @Override
    public Optional<Event> findEventByEventName(String eventName) {
        return Optional.empty();
    }

    @Override
    public void delete(Event entity) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void updateEvent(Event event) {

    }
}
