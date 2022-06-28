package ua.foxminded.task10.uml.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Classroom;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    List<Classroom> findClassroomsByNumberOrderByNumber(Integer classroomNumber);

    @Query("FROM Classroom c ORDER BY c.number")
    List<Classroom> findAll();
}
