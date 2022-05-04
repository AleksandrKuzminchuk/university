package ua.foxminded.task10.uml.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.EventDao;
import ua.foxminded.task10.uml.model.Event;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

public class EventDaoImpl implements EventDao {

    private static final Logger logger = Logger.getLogger(EventDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Event> mapper;

    public EventDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mapper = new BeanPropertyRowMapper<>(Event.class);
    }

    @Override
    public Optional<Event> save(Event event) {
        requireNonNull(event);
        logger.info(format("SAVING... %s", event));
        final String SAVE_LESSON = "INSERT INTO events (date_time, subject_id, classroom_id, teacher_id, group_id) VALUES(?,?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_LESSON, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, Timestamp.valueOf(event.getLocalDateTime()));
            statement.setInt(2, event.getSubject().getId());
            statement.setInt(3, event.getClassroom().getId());
            statement.setInt(4, event.getTeacher().getId());
            statement.setInt(5, event.getGroup().getId());
            return statement;
        }, holder);
        Integer eventId = requireNonNull(holder.getKey()).intValue();
        event.setId(eventId);
        Optional<Event> result = Optional.of(event);
        logger.info(format("SAVED %s SUCCESSFULLY", result));
        return result;
    }

    @Override
    public Optional<Event> findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FINDING... EVENT BY ID - %d", id));
        final String FIND_BY_ID = "SELECT * FROM events WHERE event_id = ?";
        Event result = jdbcTemplate.queryForObject(FIND_BY_ID, mapper, id);
        logger.info(format("FOUND %s BY ID - %d", result, id));
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECKING... EXISTS EVENT BY ID - %d", id));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM events WHERE event_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info(format("EVENT BY ID - %d EXISTS - %s", id, exists));
        return exists;
    }

    @Override
    public List<Event> findAll() {
        logger.info("FINDING... ALL EVENTS");
        final String FIND_ALL = "SELECT * FROM events";
        List<Event> events = jdbcTemplate.query(FIND_ALL, mapper);
        logger.info(format("FOUND ALL EVENTS - %d", events.size()));
        return events;
    }

    @Override
    public Long count() {
        logger.info("FINDING COUNT EVENTS");
        final String COUNT = "SELECT COUNT(*) FROM events";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT(%d) EVENTS SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETING EVENTS BY ID - %d", id));
        final String DELETE_BY_ID = "DELETE FROM events WHERE events_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, mapper);
        logger.info(format("DELETED EVENTS BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void saveAll(List<Event> events) {
        requireNonNull(events);
        logger.info(format("SAVING... EVENTS - %d", events.size()));
        events.forEach(this::save);
        logger.info(format("SAVED EVENTS - %d SUCCESSFULLY", events.size()));
    }

    @Override
    public void delete(Event event) {
        requireNonNull(event);
        logger.info(format("DELETING... %s", event));
        this.deleteById(event.getId());
        logger.info(format("DELETED %s SUCCESSFULLY", event));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL EVENTS");
        final String DELETE_ALL = "DELETE FROM events";
        jdbcTemplate.update(DELETE_ALL, mapper);
        logger.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void updateEvent(Event event) {
        requireNonNull(event);
        logger.info(format("UPDATING EVENT BY ID - %d", event.getId()));
        final String UPDATE_LESSON = "UPDATE events SET " +
                "date_time = ?, " +
                "subject_id = ?, " +
                "classroom_id = ?, " +
                "teacher_id = ?, " +
                "group_id = ? " +
                "WHERE event_id = ?";
        jdbcTemplate.update(UPDATE_LESSON, new Object[]{
                Timestamp.valueOf(event.getLocalDateTime()),
                event.getSubject().getId(),
                event.getClassroom().getId(),
                event.getTeacher().getId(),
                event.getGroup().getId(),
                event.getId()}, mapper);
        logger.info(format("UPDATED EVENT BY ID - %d SUCCESSFULLY", event.getId()));
    }

    @Override
    public List<Event> findEvents(LocalDateTime from, LocalDateTime to) {
        requireNonNull(from);
        requireNonNull(to);
        logger.info(format("FINDING EVENTS FROM %s TO %s", from.format(formatter), to.format(formatter)));

        final String FIND_EVENTS = "SELECT " +
                "date_time, " +
                "teach.first_name, " +
                "teach.last_name, " +
                "group_name, " +
                "st.first_name, " +
                "st.last_name, " +
                "course, " +
                "room_number, " +
                "subject_name " +
                "FROM events ev " +
                "JOIN  teachers teach ON (ev.teacher_id = teach.teacher_id) " +
                "JOIN teachers_subjects ts ON (teach.teacher_id = ts._teacher_id) " +
                "JOIN subjects subj ON (ts.subject_id = subj.subject_id) " +
                "JOIN groups gr ON (ev.group_id = gr.group_id) " +
                "JOIN students st (gr.group_id = st.group_id) " +
                "WHERE date_time BETWEEN '?' AND '?'";
        List<Event> events = jdbcTemplate.query(FIND_EVENTS, mapper, from.format(formatter), to.format(formatter));
        logger.info(format("FOUND %d FROM %s TO %s", events.size(), from.format(formatter), to.format(formatter)));
        return events;
    }
}
