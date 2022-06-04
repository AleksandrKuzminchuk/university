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
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class StudentDaoImpl implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

    private static final String GENERATE_TEMPLATE =
            "SELECT student_id, first_name, last_name, course, st.group_id, g.group_name " +
            "FROM students st " +
            "LEFT OUTER JOIN groups g " +
            "ON st.group_id = g.group_id ";

    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper;

    @Autowired
    public StudentDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentRowMapper = new StudentRowMapper(new GroupRowMapper());
    }

    @Override
    public Optional<Student> save(Student student) {
        requireNonNull(student);
        logger.info("SAVING {}", student);
        final String SAVE_STUDENT = "INSERT INTO students (first_name, last_name, course) VALUES (INITCAP(?),INITCAP(?),?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_STUDENT, new String[]{"student_id"});
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getCourse());
            return statement;
        }, holder);
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
        final String FIND_BY_ID = GENERATE_TEMPLATE + "WHERE student_id = ?";
        Student result = jdbcTemplate.queryForObject(FIND_BY_ID, studentRowMapper, id);
        logger.info("FOUND {} BY ID SUCCESSFULLY", result);
        return Optional.ofNullable(result);
    }


    @Override
    public Optional<Student> findStudentByNameSurname(Student student) {
        requireNonNull(student);
        logger.info("FIND STUDENT {}", student);
        final String FIND_BY_NAME_SURNAME = GENERATE_TEMPLATE + "WHERE first_name = INITCAP(?) AND last_name = INITCAP(?)";
        Student result = jdbcTemplate.queryForObject(FIND_BY_NAME_SURNAME, studentRowMapper,
                student.getFirstName(), student.getLastName());
        logger.info("FOUND STUDENT {}", result);
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
        final String FIND_ALL = GENERATE_TEMPLATE;
        List<Student> students = jdbcTemplate.query(FIND_ALL, studentRowMapper);
        logger.info("FOUND ALL STUDENTS: {}", students.size());
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
    public void deleteStudentsByCourseNumber(Integer courseNumber) {
        requireNonNull(courseNumber);
        logger.info("DELETE STUDENTS BY COURSE NUMBER - {}", courseNumber);
        final String DELETE_BY_COURSE_NUMBER = "DELETE FROM students WHERE course = ?";
        jdbcTemplate.update(DELETE_BY_COURSE_NUMBER, courseNumber);
        logger.info("DELETED STUDENTS BY COURSE NUMBER - {} SUCCESSFULLY", courseNumber);
    }

    @Override
    public void deleteStudentsByGroupId(Integer groupId) {
        requireNonNull(groupId);
        logger.info("DELETE STUDENTS BY GROUP ID - {}", groupId);
        final String DELETE_BY_GROUP_NAME = "UPDATE students SET group_id = null WHERE group_id = ?";
        jdbcTemplate.update(DELETE_BY_GROUP_NAME, groupId);
        logger.info("DELETED STUDENTS BY GROUP ID - {} SUCCESSFULLY", groupId);
    }

    @Override
    public void delete(Student student) {
        requireNonNull(student);
        logger.info("DELETE {}...", student);
        final String DELETE_STUDENT = "DELETE FROM students WHERE first_name = INITCAP(?) AND last_name = INITCAP(?)";
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
        final String FIND_STUDENTS_BY_COURSE_NUMBER = GENERATE_TEMPLATE + "WHERE course = ?";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_COURSE_NUMBER, studentRowMapper, courseNumber);
        logger.info("FOUND {} BY COURSE NUMBER - {}", students, courseNumber);
        return students;
    }

    @Override
    public void updateStudent(Integer studentId, Student updatedStudent) {
        requireNonNull(studentId);
        requireNonNull(updatedStudent);
        logger.info("UPDATING... STUDENT BY ID - {}", studentId);
        final String UPDATE_STUDENT = "UPDATE students SET " +
                "first_name = INITCAP(?), " +
                "last_name = INITCAP(?), " +
                "course = ? " +
                "WHERE student_id = ?";
        jdbcTemplate.update(UPDATE_STUDENT, updatedStudent.getFirstName(),
                updatedStudent.getLastName(), updatedStudent.getCourse(), studentId);
        logger.info("UPDATED {} SUCCESSFULLY", updatedStudent);
    }

    @Override
    public void updateTheStudentGroup(Integer groupId, Integer studentId) {
        requireNonNull(groupId);
        requireNonNull(studentId);
        logger.info("UPDATE THE STUDENTS' BY ID - {} GROUP BY ID - {}", studentId, groupId);
        final String DELETE_THE_STUDENT_GROUP = "UPDATE students SET group_id = ? WHERE student_id = ?";
        jdbcTemplate.update(DELETE_THE_STUDENT_GROUP, groupId, studentId);
        logger.info("UPDATED THE STUDENTS' BY ID - {} GROUP BY ID - {} SUCCESSFULLY", studentId, groupId);
    }

    @Override
    public void deleteTheStudentGroup(Integer studentId) {
        requireNonNull(studentId);
        logger.info("DELETE THE STUDENTS' BY ID - {} GROUP", studentId);
        final String UPDATE_THE_STUDENT_GROUP = "UPDATE students SET group_id = null WHERE student_id = ?";
        jdbcTemplate.update(UPDATE_THE_STUDENT_GROUP, studentId);
        logger.info("DELETED THE STUDENTS' BY ID - {} GROUP", studentId);
    }

    @Override
    public List<Student> findStudentsByGroupName(Group groupName) {
        requireNonNull(groupName.getName());
        logger.info("FINDING STUDENTS FROM GROUP NAME - {}", groupName.getName());
        final String FIND_STUDENTS_BY_GROUP_ID = GENERATE_TEMPLATE + "WHERE g.group_name = UPPER(?)";
        List<Student> students = jdbcTemplate.query(FIND_STUDENTS_BY_GROUP_ID, studentRowMapper, groupName.getName());
        logger.info("FOUND {} FROM GROUP NAME - {}", students.size(), groupName.getName());
        return students;
    }
}
