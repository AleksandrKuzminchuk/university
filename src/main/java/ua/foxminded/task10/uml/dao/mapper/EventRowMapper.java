package ua.foxminded.task10.uml.dao.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class EventRowMapper implements RowMapper<Event> {
    SubjectRowMapper subjectRowMapper;
    ClassroomRowMapper classroomRowMapper;
    TeacherRowMapper teacherRowMapper;
    GroupRowMapper groupRowMapper;

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event(rs.getInt("event_id"));
        LocalDateTime dateTime = rs.getObject("date_time", LocalDateTime.class);
        event.setDateTime(dateTime);
        Subject subject = subjectRowMapper.mapRow(rs, rowNum);
        event.setSubject(subject);
        Classroom classroom = classroomRowMapper.mapRow(rs, rowNum);
        event.setClassroom(classroom);
        Teacher teacher = teacherRowMapper.mapRow(rs, rowNum);
        event.setTeacher(teacher);
        Group group = groupRowMapper.mapRow(rs, rowNum);
        event.setGroup(group);
        return event;
    }
}