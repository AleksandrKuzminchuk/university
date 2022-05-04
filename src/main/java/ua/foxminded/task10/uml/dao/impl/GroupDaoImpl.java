package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.GroupDao;
import ua.foxminded.task10.uml.dao.StudentDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.organization.Group;
import ua.foxminded.task10.uml.model.people.Student;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

public class GroupDaoImpl implements GroupDao {

    private static final Logger logger = Logger.getLogger(GroupDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    private StudentDao studentDao;

    public GroupDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Group> save(Group group) {
        requiredNonNull(group);
        logger.info(format("SAVING %s...", group));
        final String SAVE_GROUP = "INSERT INTO groups (group_name) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_GROUP, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, group.getName());
            return statement;
        }, holder);
        Integer groupId = Objects.requireNonNull(holder.getKey()).intValue();
        group.setId(groupId);
        Optional<Group> result = Optional.of(group);
        logger.info(format("%s SAVED SUCCESSFULLY", group));
        return result;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        requiredNonNull(id);
        logger.info(format("FINDING GROUP BY ID - %d", id));
        final String FIND_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
        Group result = jdbcTemplate.queryForObject(
                FIND_BY_ID, new BeanPropertyRowMapper<>(Group.class), id);
        logger.info(format("FOUND %s BY ID - %d", result, id));
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requiredNonNull(id);
        logger.info(format("CHECKING... GROUP EXISTS BY ID - %d", id));
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM groups WHERE group_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info(format("GROUP BY ID - %d EXISTS - %s", id, exists));
        return exists;
    }

    @Override
    public List<Group> findAll() {
        logger.info("FINDING ALL GROUPS");
        final String FIND_GROUPS = "SELECT * FROM groups";
        List<Group> groups = jdbcTemplate.query(FIND_GROUPS, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("FOUND ALL GROUPS: %s", groups));
        return groups;
    }

    @Override
    public Long count() {
        logger.info("FINDING COUNT ALL GROUPS");
        final String COUNT = "SELECT COUNT(*) FROM groups";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        assert count != null;
        logger.info(format("FOUND COUNT(%d) GROUPS SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requiredNonNull(id);
        logger.info(format("DELETE GROUP BY ID - %d", id));
        final String DELETE_BY_ID = "DELETE FROM groups WHERE group_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{id}, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("DELETED GROUP BY ID - %d", id));
    }

    @Override
    public void delete(Group group) {
        requiredNonNull(group);
        logger.info(format("DELETE GROUP %s", group));
        final String DELETE_GROUP = "DELETE FROM groups WHERE group_name = ?";
        jdbcTemplate.update(DELETE_GROUP, new Object[]{group.getName()}, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("DELETED GROUP %s", group));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL GROUPS");
        final String DELETE_GROUPS = "DELETE FROM groups";
        jdbcTemplate.update(DELETE_GROUPS, new BeanPropertyRowMapper<>(Group.class));
        logger.info("DELETED ALL GROUPS");
    }

    @Override
    public void saveAll(List<Group> groups) {
        requiredNonNull(groups);
        logger.info(format("SAVING GROUPS - %d", groups.size()));
        groups.forEach(this::save);
        logger.info(format("SAVED GROUPS - %d SUCCESSFULLY ", groups.size()));
    }

    @Override
    public Optional<Group> findByGroupName(String groupName) {
        requiredNonNull(groupName);
        logger.info(format("FINDING GROUP BY NAME - %s", groupName));
        final String FIND_GROUP_BY_NAME = "SELECT * FROM groups WHERE group_name = ?";
        Group group = jdbcTemplate.queryForObject(FIND_GROUP_BY_NAME
                , new BeanPropertyRowMapper<>(Group.class), groupName);
        logger.info(format("FOUND %s BY NAME %s", group, groupName));
        return Optional.ofNullable(group);
    }

    @Override
    public void updateGroup(Group group) {
        requiredNonNull(group);
        logger.info(format("UPDATING GROUP BY ID - %d", group.getId()));
        final String UPDATE_GROUP = "UPDATE groups SET group_name = ? WHERE group_id = ?";
        jdbcTemplate.update(UPDATE_GROUP, new Object[]{group.getName(), group.getId()}, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("UPDATED %s SUCCESSFULLY", group));
    }

    @Override
    public void assignStudentToGroup(Integer studentId, Integer groupId) {
        requiredNonNull(studentId);
        requiredNonNull(groupId);
        logger.info(format("ASSIGN %s TO %s", studentId, groupId));
        final String ASSIGN_STUDENT_TO_GROUP = "UPDATE students SET group_id = ? WHERE student_id = ?";
        jdbcTemplate.update(ASSIGN_STUDENT_TO_GROUP, groupId, studentId,
                new BeanPropertyRowMapper<>(Student.class), new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("ASSIGNED %s TO %s SUCCESSFULLY", studentId, groupId));
    }


    @Override
    public void assignStudentsToGroup(List<Student> students, Integer groupId) {
        requiredNonNull(students);
        requiredNonNull(groupId);
        logger.info(format("ASSIGN %d TO GROUP  - %d", students.size(), groupId));
        students.forEach(student -> assignStudentToGroup(student.getId(), groupId));
        logger.info(format("ASSIGNED %d TO GROUP - %d SUCCESSFULLY", students.size(), groupId));
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
