package ua.foxminded.task10.uml.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dao.GroupDao;
import ua.foxminded.task10.uml.dao.impl.mapper.GroupRowMapper;
import ua.foxminded.task10.uml.dao.impl.mapper.StudentRowMapper;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class GroupDaoImpl implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final GroupRowMapper groupMapper;
    private final StudentRowMapper studentMapper;

    @Autowired
    public GroupDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.groupMapper = new GroupRowMapper();
        this.studentMapper = new StudentRowMapper();
    }

    @Override
    public Optional<Group> save(Group group) {
        requireNonNull(group);
        logger.info("SAVING {}...", group);
        final String SAVE_GROUP = "INSERT INTO groups (group_name) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_GROUP, new String[]{"group_id"});
            statement.setString(1, group.getName());
            return statement;
        }, holder);
        Integer groupId = requireNonNull(holder.getKey()).intValue();
        group.setId(groupId);
        Optional<Group> result = Optional.of(group);
        logger.info("SAVED {} SUCCESSFULLY", group);
        return result;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        requireNonNull(id);
        logger.info("FINDING GROUP BY ID - {}", id);
        final String FIND_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
        Group result = jdbcTemplate.queryForObject(FIND_BY_ID, groupMapper, id);
        logger.info("FOUND {} BY ID - {}", result, id);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        requireNonNull(id);
        logger.info("CHECKING... GROUP EXISTS BY ID - {}", id);
        final String EXISTS_BY_ID = "SELECT COUNT(*) FROM groups WHERE group_id = ?";
        Long count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Long.class, id);
        boolean exists = count != null && count > 0;
        logger.info("GROUP BY ID - {} EXISTS - {}", id, exists);
        return exists;
    }

    @Override
    public List<Group> findAll() {
        logger.info("FINDING ALL GROUPS");
        final String FIND_GROUPS = "SELECT * FROM groups";
        List<Group> groups = jdbcTemplate.query(FIND_GROUPS, groupMapper);
        logger.info("FOUND ALL GROUPS: {}", groups);
        return groups;
    }

    @Override
    public Long count() {
        logger.info("FINDING COUNT ALL GROUPS");
        final String COUNT = "SELECT COUNT(*) FROM groups";
        Long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info("FOUND COUNT({}) GROUPS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        requireNonNull(id);
        logger.info("DELETE GROUP BY ID - {}", id);
        final String DELETE_BY_ID = "DELETE FROM groups WHERE group_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, id);
        logger.info("DELETED GROUP BY ID - {}", id);
    }

    @Override
    public void delete(Group group) {
        requireNonNull(group);
        logger.info("DELETE GROUP {}", group);
        final String DELETE_GROUP = "DELETE FROM groups WHERE group_name = ?";
        jdbcTemplate.update(DELETE_GROUP, group.getName());
        logger.info("DELETED GROUP {}", group);
    }

    @Override
    public void deleteAll() {
        logger.info("DELETE ALL GROUPS");
        final String DELETE_GROUPS = "DELETE FROM groups";
        jdbcTemplate.update(DELETE_GROUPS);
        logger.info("DELETED ALL GROUPS");
    }

    @Override
    public void saveAll(List<Group> groups) {
        requireNonNull(groups);
        logger.info("SAVING GROUPS - {}", groups.size());
        groups.forEach(this::save);
        logger.info("SAVED GROUPS - {} SUCCESSFULLY ", groups.size());
    }

    @Override
    public Optional<Group> findByGroupName(String groupName) {
        requireNonNull(groupName);
        logger.info("FINDING GROUP BY NAME - {}", groupName);
        final String FIND_GROUP_BY_NAME = "SELECT * FROM groups WHERE group_name = ?";
        Group group = jdbcTemplate.queryForObject(FIND_GROUP_BY_NAME, groupMapper, groupName);
        logger.info("FOUND {} BY NAME {}", group, groupName);
        return Optional.ofNullable(group);
    }

    @Override
    public void updateGroup(Group group) {
        requireNonNull(group);
        logger.info("UPDATING GROUP BY ID - {}", group.getId());
        final String UPDATE_GROUP = "UPDATE groups SET group_name = ? WHERE group_id = ?";
        jdbcTemplate.update(UPDATE_GROUP, group.getName(), group.getId());
        logger.info("UPDATED {} SUCCESSFULLY", group);
    }

    @Override
    public void assignStudentToGroup(Student studentId, Group groupId) {
        requireNonNull(studentId);
        requireNonNull(groupId);
        logger.info("ASSIGN {} TO {}", studentId, groupId);
        final String ASSIGN_STUDENT_TO_GROUP = "UPDATE students SET group_id = ? WHERE student_id = ?";
        jdbcTemplate.update(ASSIGN_STUDENT_TO_GROUP, groupId.getId(), studentId.getId());
        logger.info("ASSIGNED {} TO {} SUCCESSFULLY", studentId, groupId);
    }


    @Override
    public void assignStudentsToGroup(List<Student> students, Group groupId) {
        requireNonNull(students);
        requireNonNull(groupId);
        logger.info("ASSIGN {} TO GROUP  - {}", students.size(), groupId);
        students.forEach(student -> assignStudentToGroup(student, groupId));
        logger.info("ASSIGNED {} TO GROUP - {} SUCCESSFULLY", students.size(), groupId);
    }
}
