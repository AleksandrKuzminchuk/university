package ua.foxminded.task10.uml.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>, CustomizedStudent {

    Long countByGroupId(Integer groupId);

    void deleteStudentsByCourse(Integer courseNumber);

    List<Student> findStudentsByGroupIdOrderByFirstName(Integer groupId);

    List<Student> findStudentsByCourseOrderByFirstName(Integer courseNumber);

    @Query("SELECT s FROM Student s ORDER BY s.firstName, s.lastName")
    List<Student> findAll();

}
