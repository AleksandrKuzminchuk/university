package ua.foxminded.task10.uml.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.dao.ClassroomDao;
import ua.foxminded.task10.uml.model.Classroom;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClassroomDaoImpl implements ClassroomDao {

    SessionFactory sessionFactory;

    @Override
    public void saveAll(List<Classroom> classrooms) {
        requireNonNull(classrooms);
        log.info("SAVING CLASSROOMS (count = {})", classrooms.size());
        classrooms.forEach(this::save);
        log.info("SAVED {} SUCCESSFULLY", classrooms.size());
    }

    @Override
    public void updateClassroom(Integer classroomId, Classroom classroom) {
        requireNonNull(classroom);
        requireNonNull(classroomId);
        log.info("UPDATING... CLASSROOM BY ID - {}", classroomId);
        Session session = sessionFactory.getCurrentSession();
        Classroom classroomToBeUpdated = session.get(Classroom.class, classroomId);
        classroomToBeUpdated.setNumber(classroom.getNumber());
        log.info("UPDATED CLASSROOM - {} SUCCESSFULLY", classroom);
    }

    @Override
    public Optional<Classroom> save(Classroom classroom) {
        requireNonNull(classroom);
        log.info("SAVING... {}", classroom);
        Session session = sessionFactory.getCurrentSession();
        session.persist(classroom);
        log.info("SAVED {} SUCCESSFULLY", classroom);
        return Optional.of(classroom);
    }

    @Override
    public Optional<Classroom> findById(Integer classroomId) {
        requireNonNull(classroomId);
        log.info("FINDING... CLASSROOM BY ID - {}", classroomId);
        Session session = sessionFactory.getCurrentSession();
        Classroom classroom = session.get(Classroom.class, classroomId);
        log.info("FOUND SUCCESSFULLY {} BY ID - {}", classroom, classroomId);
        return Optional.ofNullable(classroom);
    }

    @Override
    public boolean existsById(Integer classroomId) {
        requireNonNull(classroomId);
        log.info("CHECKING CLASSROOM EXISTS BY ID - {}", classroomId);
        final String EXISTS_BY_ID = "SELECT COUNT(*) from Classroom c WHERE c.id=:id";
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(EXISTS_BY_ID);
        query.setParameter("id", classroomId);
        Long count = query.getSingleResult();
        boolean exists = count != null && count > 0;
        log.info("CLASSROOM BY ID - {} EXISTS - {}", classroomId, exists);
        return exists;
    }

    @Override
    public List<Classroom> findAll() {
        log.info("FINDING... ALL CLASSROOMS");
        final String FIND_ALL = "FROM Classroom ORDER BY number";
        Session session = sessionFactory.getCurrentSession();
        List<Classroom> classrooms = session.createQuery(FIND_ALL, Classroom.class).getResultList();
        log.info("FOUND ALL SUCCESSFULLY {}", classrooms.size());
        return classrooms;
    }

    @Override
    public Long count() {
        log.info("FINDING COUNT CLASSROOMS");
        final String COUNT = "SELECT COUNT(*) FROM Classroom";
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(COUNT);
        Long count = query.getSingleResult();
        log.info("FOUND COUNT({}) CLASSROOMS SUCCESSFULLY", count);
        return count;
    }

    @Override
    public void deleteById(Integer classroomId) {
        requireNonNull(classroomId);
        log.info("DELETING... CLASSROOM BY ID - {}", classroomId);
        final String DELETE_BY_ID = "DELETE FROM Classroom c WHERE c.id=:id";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(DELETE_BY_ID).setParameter("id", classroomId).executeUpdate();
        log.info("DELETED SUCCESSFULLY CLASSROOM BY ID - {}", classroomId);
    }

    @Override
    public void delete(Classroom classroom) {
        throw new NotImplementedException("The method delete not implemented");
    }

    @Override
    public void deleteAll() {
        log.info("DELETING ALL CLASSROOMS");
        final String DELETE_ALL = "DELETE FROM Classroom";
        Session session = sessionFactory.getCurrentSession();
        Query<Classroom> query = session.createQuery(DELETE_ALL);
        query.executeUpdate();
        log.info("DELETED ALL CLASSROOMS SUCCESSFULLY");
    }

    @Override
    public List<Classroom> findClassroomsByNumber(Integer classroomNumber) {
        requireNonNull(classroomNumber);
        log.info("FIND CLASSROOMS BY NUMBER - {}", classroomNumber);
        final String FIND_BY_NUMBER = "FROM Classroom c WHERE c.number=:classroomNumber";
        Session session = sessionFactory.getCurrentSession();
        Query<Classroom> query = session.createQuery(FIND_BY_NUMBER);
        query.setParameter("classroomNumber", classroomNumber);
        List<Classroom> classrooms = query.list();
        log.info("FOUND {} CLASSROOMS BY NUMBER - {} SUCCESSFULLY", classrooms.size(), classroomNumber);
        return classrooms;
    }
}
