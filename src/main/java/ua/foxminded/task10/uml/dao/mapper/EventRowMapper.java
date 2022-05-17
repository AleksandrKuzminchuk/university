package ua.foxminded.task10.uml.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.model.Event;
import ua.foxminded.task10.uml.model.Subject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

//ToDo:

@Component
public class EventRowMapper implements RowMapper<Event> {
    private final SubjectRowMapper subjectRowMapper;
    //...add other here

    @Autowired
    public EventRowMapper (SubjectRowMapper subjectRowMapper) {
        this.subjectRowMapper = subjectRowMapper;
//...add other here
}

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event(rs.getInt("event_id"));
        LocalDateTime dateTime = rs.getObject("date_time", LocalDateTime.class);
        event.setLocalDateTime(dateTime);
        Subject subject = subjectRowMapper.mapRow(rs, rowNum);
        event.setSubject(subject);
        //...add other here
        return event;
    }
}