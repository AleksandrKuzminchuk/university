package ua.foxminded.task10.uml.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.EventDao;
import ua.foxminded.task10.uml.dao.mapper.EventRowMapper;
import ua.foxminded.task10.uml.model.Event;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

@Component
public class EventDaoImpl implements EventDao {

    private static final Logger logger = LoggerFactory.getLogger(EventDaoImpl.class);

    private static final String GENERATE_TEMPLATE = "SELECT * FROM events ev " +
            "LEFT JOIN classrooms c on c.classroom_id = ev.classroom_id " +
            "LEFT JOIN subjects s on ev.subject_id = s.subject_id " +
            "LEFT JOIN teachers t on t.teacher_id = ev.teacher_id " +
            "LEFT JOIN groups g on ev.group_id = g.group_id ";

    private final JdbcTemplate jdbcTemplate;
    private final EventRowMapper eventRowMapper;

    @Autowired
    public EventDaoImpl(DataSource dataSource, EventRowMapper eventRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.eventRowMapper = eventRowMapper;
    }

    @Override
    public Optional<Event> save(Event event) {
        requireNonNull(event);
        logger.info("SAVING... {}", event);
        final String SAVE_LESSON = "INSERT INTO events (date_time, subject_id, classroom_id, teacher_id, group_id) VALUES(?,?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_LESSON, new String[]{"event_id"});
            statement.setObject(1, Timestamp.valueOf(event.getDateTime()));
            statement.setInt(2, event.getSubject().getId());
            statement.setInt(3, event.getClassroom().getId());
            statement.setInt(4, event.getTeacher().getId());
            statement.setInt(5, event.getGroup().getId());
            return statement;
        }, holder);
        Integer eventId = requireNonNull(holder.getKey()).intValue();
        event.setId(eventId);
        Optional<Event> result = Optional.of(event);
        logger.info("SAVED {} SUCCESSFULLY", result);
        return result;
    }

    @Override
    public Optional<Event> findById(Integer id) {
        requireNonNull(id);
        logger.info("FINDING... EVENT BY ID - {}", id);
        final String FIND_BY_ID = GENERATE_TEMPLATE + " WHERE event_id = ?";
        Event result = jdbcTemplate.queryForObject(FIND_BY_ID, eventRowMapper, id);
        logger.info("FOUND {} BY ID - {}", result, id);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... EXISTS EVENT BY ID - {}", id);
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM events WHERE event_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info("EVENT BY ID - {} EXISTS - {}", id, exists);
        return exists;
    }

    @Override
    public List<Event> findAll() {
        logger.info("FINDING... ALL EVENTS");
        List<Event> events = jdbcTemplate.query(GENERATE_TEMPLATE, eventRowMapper);
        logger.info("FOUND ALL EVENTS - {}", events);
        return events;
    }

    @Override
    public Long count() {
        logger.info("FINDING COUNT EVENTS");
        final String COUNT = "SELECT COUNT(*) FROM events";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info("FOUND COUNT({}) EVENTS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETING EVENTS BY ID - {}", id);
        final String DELETE_BY_ID = "DELETE FROM events WHERE event_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, id);
        logger.info("DELETED EVENTS BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void saveAll(List<Event> events) {
        requireNonNull(events);
        logger.info("SAVING... EVENTS - {}", events.size());
        events.forEach(this::save);
        logger.info("SAVED EVENTS - {} SUCCESSFULLY", events.size());
    }

    @Override
    public void delete(Event event) {
        requireNonNull(event);
        logger.info("DELETING... {}", event);
        this.deleteById(event.getId());
        logger.info("DELETED {} SUCCESSFULLY", event);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL EVENTS");
        final String DELETE_ALL = "DELETE FROM events";
        jdbcTemplate.update(DELETE_ALL);
        logger.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void updateEvent(Integer eventId, Event event) {
        requireNonNull(event);
        requireNonNull(eventId);
        logger.info("UPDATING EVENT BY ID - {}", eventId);
        final String UPDATE_EVENT = "UPDATE events SET " +
                "date_time = ?, " +
                "subject_id = ?, " +
                "classroom_id = ?, " +
                "teacher_id = ?, " +
                "group_id = ? " +
                "WHERE event_id = ?";
        jdbcTemplate.update(UPDATE_EVENT, Timestamp.valueOf(event.getDateTime()),
                event.getSubject().getId(),
                event.getClassroom().getId(),
                event.getTeacher().getId(),
                event.getGroup().getId(),
                eventId);
        logger.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventId);
    }

    @Override
    public List<Event> findEvents(LocalDateTime from, LocalDateTime to) {
        requireNonNull(from);
        requireNonNull(to);
        logger.info("FINDING EVENTS FROM {} TO {}", Timestamp.valueOf(from), Timestamp.valueOf(to));
        final String FIND_EVENTS = GENERATE_TEMPLATE + "WHERE date_time BETWEEN ? AND ?";
        List<Event> events = jdbcTemplate.query(FIND_EVENTS, eventRowMapper, Timestamp.valueOf(from), Timestamp.valueOf(to));
        logger.info("FOUND {} FROM {} TO {}", events.size(), from.format(formatter), to.format(formatter));
        return events;
    }
}
