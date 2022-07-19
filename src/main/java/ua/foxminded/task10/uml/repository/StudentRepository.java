package ua.foxminded.task10.uml.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Long countByGroupId(Integer groupId);


    void deleteByCourse(Integer courseNumber);

    List<Student> findByGroupIdOrderByFirstName(Integer groupId);

    List<Student> findByCourseOrderByFirstName(Integer courseNumber);

    List<Student> findAllByGroup_Name(String groupName, Sort firstName);

    List<Student> findByFirstNameOrLastNameOrderByFirstName(String firstName, String lastName);

    @Modifying
    @Query("delete from Student s where s.group.id = ?1")
    void deleteByGroupId(Integer groupId);

    Long countByCourse(Integer course);
}
