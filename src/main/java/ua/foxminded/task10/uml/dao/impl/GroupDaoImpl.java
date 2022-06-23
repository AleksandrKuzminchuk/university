package ua.foxminded.task10.uml.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.dao.GroupDao;
import ua.foxminded.task10.uml.dao.mapper.GroupRowMapper;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Repository
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class GroupDaoImpl implements GroupDao {

    SessionFactory sessionFactory;

    @Override
    public Optional<Group> save(Group group) {
        requireNonNull(group);
        log.info("SAVING {}...", group);
        Session session = sessionFactory.getCurrentSession();
        session.persist(group);
        log.info("SAVED {} SUCCESSFULLY", group);
        return Optional.of(group);
    }

    @Override
    public Optional<Group> findById(Integer groupId) {
        requireNonNull(groupId);
        log.info("FINDING GROUP BY ID - {}", groupId);
        Session session = sessionFactory.getCurrentSession();
        Group result = session.find(Group.class, groupId);
        log.info("FOUND {} BY ID - {}", result, groupId);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer groupId) {
        requireNonNull(groupId);
        log.info("CHECKING... GROUP EXISTS BY ID - {}", groupId);
        final String EXISTS_BY_ID = "SELECT COUNT(g) FROM Group g WHERE g.id =:groupId";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(EXISTS_BY_ID, Long.class).setParameter("groupId", groupId).uniqueResult();
        boolean exists = count != null && count > 0;
        log.info("GROUP BY ID - {} EXISTS - {}", groupId, exists);
        return exists;
    }

    @Override
    public List<Group> findAll() {
        log.info("FINDING ALL GROUPS");
        final String FIND_GROUPS = "SELECT g FROM Group g ORDER BY g.name";
        Session session = sessionFactory.getCurrentSession();
        List<Group> groups = session.createQuery(FIND_GROUPS, Group.class).getResultList();
        log.info("FOUND ALL GROUPS: {}", groups.size());
        return groups;
    }

    @Override
    public Long count() {
        log.info("FINDING COUNT ALL GROUPS");
        final String COUNT = "SELECT COUNT(g) FROM Group g";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(COUNT, Long.class).uniqueResult();
        log.info("FOUND COUNT({}) GROUPS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer groupId) {
        requireNonNull(groupId);
        log.info("DELETE GROUP BY ID - {}", groupId);
        final String DELETE_BY_ID = "DELETE FROM Group g WHERE g.id =:groupId";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_BY_ID).setParameter("groupId", groupId).executeUpdate();
        log.info("DELETED GROUP BY ID - {}", groupId);
    }

    @Override
    public void delete(Group group) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETE ALL GROUPS");
        final String DELETE_GROUPS = "DELETE FROM Group";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_GROUPS, Group.class).executeUpdate();
        log.info("DELETED ALL GROUPS");
    }

    @Override
    public void saveAll(List<Group> groups) {
        requireNonNull(groups);
        log.info("SAVING GROUPS - {}", groups.size());
        groups.forEach(this::save);
        log.info("SAVED GROUPS - {} SUCCESSFULLY ", groups.size());
    }

    @Override
    public List<Group> findGroupsByName(String groupName) {
        requireNonNull(groupName);
        log.info("FINDING GROUPS BY NAME - {}", groupName);
        final String FIND_GROUP_BY_NAME = "SELECT g FROM Group g WHERE g.name =:groupName";
        Session session = sessionFactory.getCurrentSession();
        List<Group> groups = session.createQuery(FIND_GROUP_BY_NAME, Group.class).setParameter("groupName", groupName).getResultList();
        log.info("FOUND {} GROUPS BY NAME {}", groups.size(), groupName);
        return groups;
    }

    @Override
    public void updateGroup(Integer groupId, Group group) {
        requireNonNull(group);
        requireNonNull(groupId);
        log.info("UPDATING GROUP BY ID - {}", group.getId());
        Session session = sessionFactory.getCurrentSession();
        session.find(Group.class, groupId).setName(group.getName());
        log.info("UPDATED {} SUCCESSFULLY", group);
    }

    @Override
    public void assignStudentToGroup(Student student, Group group) {
        requireNonNull(student);
        requireNonNull(group);
        log.info("ASSIGN {} TO {}", student, group);
        Session session = sessionFactory.getCurrentSession();
        Group addGroup = session.find(Group.class, group.getId());
        Student addStudent = session.find(Student.class, student.getId());
        addGroup.getStudents().add(addStudent);
        addStudent.setGroup(addGroup);
        session.merge(addGroup);
        log.info("ASSIGNED {} TO {} SUCCESSFULLY", student, group);
    }


    @Override
    public void assignStudentsToGroup(List<Student> students, Group group) {
        requireNonNull(students);
        requireNonNull(group);
        log.info("ASSIGN {} TO GROUP  - {}", students.size(), group);
        students.forEach(student -> assignStudentToGroup(student, group));
        log.info("ASSIGNED {} TO GROUP - {} SUCCESSFULLY", students.size(), group);
    }
}
