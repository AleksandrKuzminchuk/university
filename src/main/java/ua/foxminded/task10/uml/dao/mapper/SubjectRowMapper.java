package ua.foxminded.task10.uml.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.model.Subject;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subject subject = new Subject();
        subject.setId(rs.getInt("subject_id"));
        subject.setName(rs.getString("subject_name"));
        return subject;
    }
}
