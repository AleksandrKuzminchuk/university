package ua.foxminded.task10.uml.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentRowMapper implements RowMapper<Student> {

    private final GroupRowMapper groupRowMapper;

    public StudentRowMapper(GroupRowMapper groupRowMapper) {
        this.groupRowMapper = groupRowMapper;
    }

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setCourse(rs.getInt("course"));
        Group group = groupRowMapper.mapRow(rs, rowNum);
        student.setGroup(group);
        return student;
    }
}
