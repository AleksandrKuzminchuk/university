package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.EventDao;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.curriculums.Event;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

public class EventDaoImpl implements EventDao {

    private final static Logger logger = Logger.getLogger(EventDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public EventDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Event> save(Event entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... %s", entity));
        final String SAVE_LESSON = "INSERT INTO events (date_time, subject_id, classroom_id, teacher_id, group_id)" +
                " VALUES('?',?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_LESSON, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, Timestamp.valueOf(entity.getLocalDateTime()));
            statement.setInt(2, entity.getSubject().getId());
            statement.setInt(3, entity.getClassroom().getId());
            statement.setInt(4, entity.getTeacher().getId());
            statement.setInt(5,  entity.getGroup().getId());
            return statement;
        }, holder);
        Integer eventId = holder.getKey().intValue();
        entity.setId(eventId);
        Optional<Event> result = Optional.of(entity);
        logger.info(format("SAVED %s SUCCESSFULLY", result));
        return result;
    }

    @Override
    public Optional<Event> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING... EVENT BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT * FROM events WHERE event_id = ?";
        Event event = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                        new Object[]{integer}, new BeanPropertyRowMapper<>(Event.class)))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find event by id - %d", integer)));
        logger.info(format("FOUND %s BY ID - %d", event, integer));
        return Optional.of(event);
    }

    @Override
    public boolean existsById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("CHECKING... EXISTS EVENT BY ID - %d", integer));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM events WHERE event_id = ?";
        boolean result = false;

        long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, new Object[]{integer}, Long.class);
        if (count > 0){
            result = true;
            logger.info(format("CHECKED EVENT BY ID - %d EXISTS", integer));
        }
        return result;
    }

    @Override
    public List<Event> findAll() {
        logger.info("FINDING... ALL EVENTS");
        final String FIND_ALL = "SELECT * FROM events";
        List<Event> events = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Event.class));
        logger.info(format("FOUND ALL EVENTS - %d", events.size()));
        return events;
    }

    @Override
    public long count() {
        logger.info("FINDING COUNT EVENTS");
        final String COUNT = "SELECT COUNT(*) FROM events";
        long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT - %d SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING EVENTS BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM events WHERE events_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Event.class));
        logger.info(format("DELETED EVENTS BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void saveAll(List<Event> events) {
        requiredNonNull(events);
        logger.info(format("SAVING... EVENTS - %d", events.size()));
        events.forEach(this::save);
        logger.info(format("SAVED EVENTS - %d SUCCESSFULLY", events.size()));
    }

    @Override
    public void delete(Event entity) {
        requiredNonNull(entity);
        logger.info(format("DELETING... %s", entity));
        this.deleteById(entity.getId());
        logger.info(format("DELETED %s SUCCESSFULLY", entity));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL EVENTS");
        final String DELETE_ALL = "DELETE FROM events";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(Event.class));
        logger.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void updateEvent(Event event){
        requiredNonNull(event);
        logger.info(format("UPDATING EVENT BY ID - %d", event.getId()));
        final String UPDATE_LESSON = "UPDATE events SET date_time = '?', subject_id = ?, classroom_id = ?, teacher_id = ?, " +
                "group_id = ? WHERE event_id = ?";
        jdbcTemplate.update(UPDATE_LESSON, new Object[]{Timestamp.valueOf(event.getLocalDateTime()), event.getSubject().getId(),
                event.getClassroom().getId(), event.getTeacher().getId(), event.getGroup().getId(), event.getId()},
                new BeanPropertyRowMapper<>(Event.class));
        logger.info(format("UPDATED EVENT BY ID - %d SUCCESSFULLY", event.getId()));

    }

    @Override
    public List<Event> findEvents(LocalDateTime from, LocalDateTime to) {
        requiredNonNull(from);
        requiredNonNull(to);
        logger.info(format("FINDING EVENTS FROM %s TO %s", from.format(formatter), to.format(formatter)));

        final String FIND_EVENTS = "SELECT date_time , teach.first_name, teach.last_name, group_name, " +
                "st.first_name, st.last_name, course, room_number, subject_name FROM events ev " +
                "JOIN  teachers teach ON (ev.teacher_id = teach.teacher_id) " +
                "JOIN teachers_subjects ts ON (teach.teacher_id = ts._teacher_id) " +
                "JOIN subjects subj ON (ts.subject_id = subj.subject_id)" +
                " JOIN groups gr ON (ev.group_id = gr.group_id) " +
                "JOIN students st (gr.group_id = st.group_id) WHERE date_time BETWEEN '?' AND '?'";
        List<Event> events = jdbcTemplate.query(FIND_EVENTS, new Object[]{from.format(formatter), to.format(formatter)},
                new BeanPropertyRowMapper<>(Event.class));
        logger.info(format("FOUND %d FROM %s TO %s", events.size(), from.format(formatter), to.format(formatter)));
        return events;
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
