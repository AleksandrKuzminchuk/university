package ua.foxminded.task10.uml.dao.impl.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.model.Classroom;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClassroomRowMapper implements RowMapper<Classroom> {
    @Override
    public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {
        Classroom classroom = new Classroom();
        classroom.setId(rs.getInt("classroom_id"));
        classroom.setNumber(rs.getInt("room_number"));
        return classroom;
    }
}
