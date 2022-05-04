package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.StudentDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.people.Student;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class StudentDaoImpl implements StudentDao {

    private static final Logger logger = Logger.getLogger(StudentDaoImpl.class);

    public final JdbcTemplate jdbcTemplate;

    public StudentDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Student> save(Student entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING %s", entity));
        final String SAVE_STUDENT = "INSERT INTO students (first_name, last_name, course, group_id) VALUES (?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_STUDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setInt(3, entity.getCourse());
            statement.setInt(4, entity.getGroupId());
            return statement;
        }, holder);
        Integer studentId = holder.getKey().intValue();
        entity.setId(studentId);
        Optional<Student> result = Optional.of(entity);
        logger.info(format("%s SAVED SUCCESSFULLY", entity));
        return result;
    }

    @Override
    public Optional<Student> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FIND STUDENT BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT * FROM students WHERE student_id = ?";
        Optional<Student> student = Optional.of(Optional.ofNullable(jdbcTemplate.
                        queryForObject(FIND_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Student.class)))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find student by id - %d", integer))));
        logger.info(format("FOUND %s BY ID SUCCESSFULLY", student));
        return student;
    }

    @Override
    public boolean existsById(Integer integer) {
        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<Student> findAll() {
        logger.info("FIND ALL STUDENTS...");
        final String FIND_ALL = "SELECT * FROM students";
        List<Student> students = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Student.class));
        logger.info(format("FOUND ALL STUDENTS: %s", students));
        return students;
    }

    @Override
    public long count() {
        logger.info("FIND COUNT ALL STUDENTS...");
        final String COUNT = "SELECT COUNT(*) FROM students";
        long countStudents = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT ALL STUDENTS - %d", countStudents));
        return countStudents;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETE STUDENT BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM students WHERE student_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Student.class));
        logger.info(format("DELETED STUDENT BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(Student entity) {
        requiredNonNull(entity);
        logger.info(format("DELETE %s...", entity));
        final String DELETE_STUDENT = "DELETE FROM students WHERE first_name = ? AND last_name = ?";
        jdbcTemplate.update(DELETE_STUDENT, new Object[]{entity.getFirstName(), entity.getLastName()}, new BeanPropertyRowMapper<>(Student.class));
        logger.info(format("DELETED %s SUCCESSFULLY", entity));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL STUDENTS...");
        final String DELETE_ALL_STUDENTS = "DELETE FROM students;";
        jdbcTemplate.update(DELETE_ALL_STUDENTS, new BeanPropertyRowMapper<>(Student.class));
        logger.info("DELETED ALL STUDENTS SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Student> students) {
        requiredNonNull(students);
        logger.info(format("SAVING %d STUDENTS", students.size()));
        students.forEach(this::save);
        logger.info(format("SAVED %d STUDENTS SUCCESSFULLY", students.size()));
    }

    @Override
    public List<Student> findByCourseNumber(Integer courseNumber) {
        requiredNonNull(courseNumber);
        logger.info(format("FINDING STUDENTS BY COURSE NUMBER %d", courseNumber));
        final String FIND_STUDENTS_BY_COURSE_NUMBER = "SELECT * FROM students WHERE course_number = ?";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_COURSE_NUMBER,
                new Object[]{courseNumber}, new BeanPropertyRowMapper<>(Student.class));
        logger.info(format("FOUND %s BY COURSE NUMBER - %d",students, courseNumber));
        return students;
    }

    @Override
    public void updateStudent(Student student) {
        requiredNonNull(student);
        logger.info(format("UPDATING... STUDENT BY ID - %d", student.getId()));
        final String UPDATE_STUDENT = "UPDATE students SET first_name = ?, last_name = ?, course = ?, group_id = ? " +
                "WHERE student_id = ?";
        jdbcTemplate.update(UPDATE_STUDENT, new Object[]{student.getFirstName(), student.getLastName(), student.getCourse(),
        student.getGroupId() ,student.getId()}, new BeanPropertyRowMapper<>(Student.class));
        logger.info(format("UPDATED %s SUCCESSFULLY", student));
    }

    @Override
    public List<Student> findStudentsByGroupId(Integer groupId) {
        requiredNonNull(groupId);
        logger.info(format("FINDING STUDENTS FROM GROUP ID - %d", groupId));
        final String FIND_STUDENTS_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ?";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_GROUP_ID, new Object[]{groupId}, new BeanPropertyRowMapper<>(Student.class));
        logger.info(format("FOUND %s FROM GROUP ID - %d", students.size(), groupId));
        return students;
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
