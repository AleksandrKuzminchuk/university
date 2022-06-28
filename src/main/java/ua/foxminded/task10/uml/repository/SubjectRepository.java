package ua.foxminded.task10.uml.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Subject;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer>, CustomizedSubject {

    @Query("FROM Subject s ORDER BY s.name")
    List<Subject> findAll();
}
