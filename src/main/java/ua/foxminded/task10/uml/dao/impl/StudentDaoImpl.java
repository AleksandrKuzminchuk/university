package ua.foxminded.task10.uml.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.StudentDao;
import ua.foxminded.task10.uml.dao.mapper.GroupRowMapper;
import ua.foxminded.task10.uml.dao.mapper.StudentRowMapper;
import ua.foxminded.task10.uml.model.Student;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class StudentDaoImpl implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper;
    private final GroupRowMapper groupRowMapper;

    @Autowired
    public StudentDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentRowMapper = new StudentRowMapper();
        this.groupRowMapper = new GroupRowMapper();
    }

    @Override
    public Optional<Student> save(Student student) {
        requireNonNull(student);
        logger.info("SAVING {}", student);
        final String SAVE_STUDENT = "INSERT INTO students (first_name, last_name, course) VALUES (?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_STUDENT, new String[]{"student_id"});
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getCourse());
            return statement;
        },holder);
        Integer studentId = requireNonNull(holder.getKey()).intValue();
        student.setId(studentId);
        Optional<Student> result = Optional.of(student);
        logger.info("{} SAVED SUCCESSFULLY", student);
        return result;
    }

    @Override
    public Optional<Student> findById(Integer id) {
        requireNonNull(id);
        logger.info("FIND STUDENT BY ID - {}", id);
        final String FIND_BY_ID = "SELECT * FROM students WHERE student_id = ?";
        Student result = jdbcTemplate.queryForObject(FIND_BY_ID, studentRowMapper, id);
        logger.info("FOUND {} BY ID SUCCESSFULLY", result);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... STUDENT EXISTS BY ID - {}", id);
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info("STUDENT BY ID - {} EXISTS - {}", id, exists);
        return exists;
    }

    @Override
    public List<Student> findAll() {
        logger.info("FIND ALL STUDENTS...");
        final String FIND_ALL = "SELECT * FROM students";
        List<Student> students = jdbcTemplate.query(FIND_ALL, studentRowMapper);
        logger.info("FOUND ALL STUDENTS: {}", students);
        return students;
    }

    @Override
    public Long count() {
        logger.info("FIND COUNT ALL STUDENTS...");
        final String COUNT = "SELECT COUNT(*) FROM students";
        Long countStudents = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info("FOUND COUNT({}) STUDENTS SUCCESSFULLY", countStudents);
        return countStudents;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETE STUDENT BY ID - {}", id);
        final String DELETE_BY_ID = "DELETE FROM students WHERE student_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, id);
        logger.info("DELETED STUDENT BY ID - {} SUCCESSFULLY", id);
    }

    @Override
    public void delete(Student student) {
        requireNonNull(student);
        logger.info("DELETE {}...", student);
        final String DELETE_STUDENT = "DELETE FROM students WHERE first_name = ? AND last_name = ?";
        jdbcTemplate.update(DELETE_STUDENT, student.getFirstName(), student.getLastName());
        logger.info("DELETED {} SUCCESSFULLY", student);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL STUDENTS...");
        final String DELETE_ALL_STUDENTS = "DELETE FROM students;";
        jdbcTemplate.update(DELETE_ALL_STUDENTS);
        logger.info("DELETED ALL STUDENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Student> students) {
        requireNonNull(students);
        logger.info("SAVING {} STUDENTS", students.size());
        students.forEach(this::save);
        logger.info("SAVED {} STUDENTS SUCCESSFULLY", students.size());
    }

    @Override
    public List<Student> findByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        logger.info("FINDING STUDENTS BY COURSE NUMBER {}", courseNumber);
        final String FIND_STUDENTS_BY_COURSE_NUMBER = "SELECT * FROM students WHERE course = ?";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_COURSE_NUMBER, studentRowMapper, courseNumber);
        logger.info("FOUND {} BY COURSE NUMBER - {}", students, courseNumber);
        return students;
    }

    @Override
    public void updateStudent(Student student) {
        requireNonNull(student);
        logger.info("UPDATING... STUDENT BY ID - {}", student.getId());
        final String UPDATE_STUDENT = "UPDATE students SET " +
                "first_name = ?, " +
                "last_name = ?, " +
                "course = ?, " +
                "group_id = ? " +
                "WHERE student_id = ?";
        jdbcTemplate.update(UPDATE_STUDENT, student.getFirstName(), student.getLastName(), student.getCourse(),
                student.getGroupId(), student.getId());
        logger.info("UPDATED {} SUCCESSFULLY", student);
    }

    @Override
    public List<Student> findStudentsByGroupName(Integer groupId) {
        requireNonNull(groupId);
        logger.info("FINDING STUDENTS FROM GROUP ID - {}", groupId);
        final String FIND_STUDENTS_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ?";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_GROUP_ID, studentRowMapper, groupId);
        logger.info("FOUND {} FROM GROUP ID - {}", students.size(), groupId);
        return students;
    }
}
