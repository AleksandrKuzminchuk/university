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
import java.util.Optional;

import static java.lang.String.format;

public class GroupDaoImpl implements GroupDao {

    private final static Logger logger = Logger.getLogger(GroupDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    private StudentDao studentDao;

    public GroupDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Group> save(Group entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING %s...", entity));
        final String SAVE_GROUP = "INSERT INTO groups (group_name) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_GROUP, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            return statement;
        }, holder);
        Integer groupId = holder.getKey().intValue();
        entity.setId(groupId);
        Optional<Group> result = Optional.of(entity);
        logger.info(format("%s SAVED SUCCESSFULLY", entity));
        return result;
    }

    @Override
    public Optional<Group> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING GROUP BY ID - d", integer));
        final String FIND_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
        Optional<Group> result = Optional.of(Optional.ofNullable(jdbcTemplate.queryForObject(
                FIND_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Group.class)
        )).orElseThrow(() -> new IllegalArgumentException(format("Can't find group by id - %d", integer))));
        logger.info(format("FOUND %s BY ID - %d", result, integer));
        return result;
    }

    @Override
    public boolean existsById(Integer integer) {
        throw new NotImplementedException("Method not implemented");
    }

    @Override
    public List<Group> findAll() {
        logger.info("FINDING ALL GROUPS");
        final String FIND_GROUPS = "SELECT * FROM groups;";
        List<Group> groups = jdbcTemplate.query(FIND_GROUPS, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("FOUND ALL GROUPS: %s", groups));
        return groups;
    }

    @Override
    public long count() {
        logger.info("FINDING COUNT ALL GROUPS");
        final String COUNT = "SELECT COUNT(*) FROM groups";
        long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT ALL GROUPS - %d", count));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETE GROUP BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM groups WHERE group_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("DELETED GROUP BY ID - %d", integer));
    }

    @Override
    public void delete(Group entity) {
        requiredNonNull(entity);
        logger.info(format("DELETE GROUP %s", entity));
        final String DELETE_GROUP = "DELETE FROM groups WHERE group_name = ?";
        jdbcTemplate.update(DELETE_GROUP, new Object[]{entity.getName()}, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("DELETED GROUP %s", entity));
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
        Optional<Group> group = Optional.of(Optional.ofNullable(jdbcTemplate.queryForObject(FIND_GROUP_BY_NAME,
                new Object[]{groupName}, new BeanPropertyRowMapper<>(Group.class)))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find group by name %s", groupName))));
        logger.info(format("FOUND %s BY NAME %s", group, groupName));
        return group;
    }

    @Override
    public void updateGroup(String groupName, String newGroupName) {
        requiredNonNull(groupName);
        requiredNonNull(newGroupName);
        logger.info(format("UPDATING %s", groupName));
        final String UPDATE_GROUP = "UPDATE groups SET group_name =? WHERE group_name = ?";
        jdbcTemplate.update(UPDATE_GROUP, new Object[]{newGroupName, groupName}, new BeanPropertyRowMapper<>(Group.class));
        logger.info(format("UPDATED %s", newGroupName));
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
