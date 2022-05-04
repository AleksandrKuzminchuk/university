package ua.foxminded.task10.uml.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.StudentDao;
import ua.foxminded.task10.uml.model.Student;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class StudentDaoImpl implements StudentDao {

    private static final Logger logger = Logger.getLogger(StudentDaoImpl.class);

    public final JdbcTemplate jdbcTemplate;
    public final BeanPropertyRowMapper<Student> mapper;

    public StudentDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mapper = new BeanPropertyRowMapper<>(Student.class);
    }

    @Override
    public Optional<Student> save(Student student) {
        requireNonNull(student);
        logger.info(format("SAVING %s", student));
        final String SAVE_STUDENT = "INSERT INTO students (first_name, last_name, course, group_id) VALUES (?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_STUDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getCourse());
            statement.setInt(4, student.getGroupId());
            return statement;
        }, holder);
        Integer studentId = requireNonNull(holder.getKey()).intValue();
        student.setId(studentId);
        Optional<Student> result = Optional.of(student);
        logger.info(format("%s SAVED SUCCESSFULLY", student));
        return result;
    }

    @Override
    public Optional<Student> findById(Integer id) {
        requireNonNull(id);
        logger.info(format("FIND STUDENT BY ID - %d", id));
        final String FIND_BY_ID = "SELECT * FROM students WHERE student_id = ?";
        Student result = jdbcTemplate.queryForObject(FIND_BY_ID, mapper, id);
        logger.info(format("FOUND %s BY ID SUCCESSFULLY", result));
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info(format("CHECING... STUDENT EXISTS BY ID - %d", id));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info(format("STUDENT BY ID - %d EXISTS - %s", id, exists));
        return exists;
    }

    @Override
    public List<Student> findAll() {
        logger.info("FIND ALL STUDENTS...");
        final String FIND_ALL = "SELECT * FROM students";
        List<Student> students = jdbcTemplate.query(FIND_ALL, mapper);
        logger.info(format("FOUND ALL STUDENTS: %s", students));
        return students;
    }

    @Override
    public Long count() {
        logger.info("FIND COUNT ALL STUDENTS...");
        final String COUNT = "SELECT COUNT(*) FROM students";
        Long countStudents = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT(%d) STUDENTS SUCCESSFULLY", countStudents));
        return countStudents;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info(format("DELETE STUDENT BY ID - %d", id));
        final String DELETE_BY_ID = "DELETE FROM students WHERE student_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, mapper);
        logger.info(format("DELETED STUDENT BY ID - %d SUCCESSFULLY", id));
    }

    @Override
    public void delete(Student student) {
        requireNonNull(student);
        logger.info(format("DELETE %s...", student));
        final String DELETE_STUDENT = "DELETE FROM students WHERE first_name = ? AND last_name = ?";
        jdbcTemplate.update(DELETE_STUDENT, new Object[]{student.getFirstName(), student.getLastName()}, mapper);
        logger.info(format("DELETED %s SUCCESSFULLY", student));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL STUDENTS...");
        final String DELETE_ALL_STUDENTS = "DELETE FROM students;";
        jdbcTemplate.update(DELETE_ALL_STUDENTS, mapper);
        logger.info("DELETED ALL STUDENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Student> students) {
        requireNonNull(students);
        logger.info(format("SAVING %d STUDENTS", students.size()));
        students.forEach(this::save);
        logger.info(format("SAVED %d STUDENTS SUCCESSFULLY", students.size()));
    }

    @Override
    public List<Student> findByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        logger.info(format("FINDING STUDENTS BY COURSE NUMBER %d", courseNumber));
        final String FIND_STUDENTS_BY_COURSE_NUMBER = "SELECT * FROM students WHERE course_number = ?";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_COURSE_NUMBER, mapper, courseNumber);
        logger.info(format("FOUND %s BY COURSE NUMBER - %d", students, courseNumber));
        return students;
    }

    @Override
    public void updateStudent(Student student) {
        requireNonNull(student);
        logger.info(format("UPDATING... STUDENT BY ID - %d", student.getId()));
        final String UPDATE_STUDENT = "UPDATE students SET " +
                "first_name = ?, " +
                "last_name = ?, " +
                "course = ?, " +
                "group_id = ? " +
                "WHERE student_id = ?";
        jdbcTemplate.update(UPDATE_STUDENT, new Object[]{
                student.getFirstName(),
                student.getLastName(),
                student.getCourse(),
                student.getGroupId(),
                student.getId()}, mapper);
        logger.info(format("UPDATED %s SUCCESSFULLY", student));
    }

    @Override
    public List<Student> findStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        logger.info(format("FINDING STUDENTS FROM GROUP ID - %d", groupId));
        final String FIND_STUDENTS_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ?";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_GROUP_ID, mapper, groupId);
        logger.info(format("FOUND %s FROM GROUP ID - %d", students.size(), groupId));
        return students;
    }
}
