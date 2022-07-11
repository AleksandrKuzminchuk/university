package ua.foxminded.task10.uml.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Classroom;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    Optional<Classroom> findByNumber(Integer number);
}
