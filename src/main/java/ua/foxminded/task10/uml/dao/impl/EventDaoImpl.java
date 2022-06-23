package ua.foxminded.task10.uml.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.dao.EventDao;
import ua.foxminded.task10.uml.model.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

@Repository
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class EventDaoImpl implements EventDao {

    private static final String GENERATE_TEMPLATE = "SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.classroom " +
            "LEFT JOIN FETCH e.subject " +
            "LEFT JOIN FETCH e.teacher " +
            "LEFT JOIN FETCH e.group ";
    private static final String GENERATE_TEMPLATE_ORDER_BY = "ORDER BY e.dateTime";

    SessionFactory sessionFactory;

    @Override
    public Optional<Event> save(Event event) {
        requireNonNull(event);
        log.info("SAVING... {}", event);
        Session session = sessionFactory.getCurrentSession();
        session.merge(event);
        log.info("SAVED {} SUCCESSFULLY", event);
        return Optional.of(event);
    }

    @Override
    public Optional<Event> findById(Integer eventId) {
        requireNonNull(eventId);
        log.info("FINDING... EVENT BY ID - {}", eventId);
        final String FIND_BY_ID = GENERATE_TEMPLATE + " WHERE e.id=:eventId " + GENERATE_TEMPLATE_ORDER_BY;
        Session session = sessionFactory.getCurrentSession();
        Event result = session.createQuery(FIND_BY_ID, Event.class).setParameter("eventId", eventId).uniqueResult();
        log.info("FOUND {} BY ID - {}", result, eventId);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer eventId) {
        requireNonNull(eventId);
        log.info("CHECKING... EXISTS EVENT BY ID - {}", eventId);
        final String EXISTS_BY_ID = "SELECT COUNT(e) FROM Event e WHERE e.id=:eventId";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(EXISTS_BY_ID, Long.class).setParameter("eventId", eventId).uniqueResult();
        boolean exists = count != null && count > 0;
        log.info("EVENT BY ID - {} EXISTS - {}", eventId, exists);
        return exists;
    }

    @Override
    public List<Event> findAll() {
        log.info("FINDING... ALL EVENTS");
        Session session = sessionFactory.getCurrentSession();
        List<Event> events = session.createQuery(GENERATE_TEMPLATE + GENERATE_TEMPLATE_ORDER_BY, Event.class).getResultList();
        log.info("FOUND ALL EVENTS - {}", events);
        return events;
    }

    @Override
    public Long count() {
        log.info("FINDING COUNT EVENTS");
        final String COUNT = "SELECT COUNT(e) FROM Event e";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(COUNT, Long.class).uniqueResult();
        log.info("FOUND COUNT({}) EVENTS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer eventId) {
        requireNonNull(eventId);
        log.info("DELETING EVENTS BY ID - {}", eventId);
        final String DELETE_BY_ID = "DELETE FROM Event e WHERE e.id=:eventId";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_BY_ID).setParameter("eventId", eventId).executeUpdate();
        log.info("DELETED EVENTS BY ID - {} SUCCESSFULLY", eventId);
    }

    @Override
    public void saveAll(List<Event> events) {
        requireNonNull(events);
        log.info("SAVING... EVENTS - {}", events.size());
        events.forEach(this::save);
        log.info("SAVED EVENTS - {} SUCCESSFULLY", events.size());
    }

    @Override
    public void delete(Event event) {
        requireNonNull(event);
        log.info("DELETING... {}", event);
        this.deleteById(event.getId());
        log.info("DELETED {} SUCCESSFULLY", event);
    }

    @Override
    public void deleteAll() {
        log.info("DELETING ALL EVENTS");
        final String DELETE_ALL = "DELETE FROM Event";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_ALL).executeUpdate();
        log.info("DELETED ALL EVENTS SUCCESSFULLY");
    }

    @Override
    public void updateEvent(Integer eventId, Event event) {
        requireNonNull(event);
        requireNonNull(eventId);
        log.info("UPDATING EVENT BY ID - {}", eventId);
        Session session = sessionFactory.getCurrentSession();
        Event eventToBeUpdated = session.find(Event.class, eventId);
        eventToBeUpdated.setDateTime(event.getDateTime());
        eventToBeUpdated.setSubject(event.getSubject());
        eventToBeUpdated.setClassroom(event.getClassroom());
        eventToBeUpdated.setTeacher(event.getTeacher());
        eventToBeUpdated.setGroup(event.getGroup());
        log.info("UPDATED EVENT BY ID - {} SUCCESSFULLY", eventId);
    }

    @Override
    public List<Event> findEvents(LocalDateTime from, LocalDateTime to) {
        requireNonNull(from);
        requireNonNull(to);
        log.info("FINDING EVENTS FROM {} TO {}", Timestamp.valueOf(from), Timestamp.valueOf(to));
        final String FIND_EVENTS = GENERATE_TEMPLATE + "WHERE e.dateTime BETWEEN :from AND :to " + GENERATE_TEMPLATE_ORDER_BY;
        Session session = sessionFactory.getCurrentSession();
        Query<Event> query = session.createQuery(FIND_EVENTS, Event.class);
        query.setParameter("from", from);
        query.setParameter("to",  to);
        List<Event> events = query.getResultList();
        log.info("FOUND {} FROM {} TO {}", events.size(), from.format(formatter), to.format(formatter));
        return events;
    }
}
