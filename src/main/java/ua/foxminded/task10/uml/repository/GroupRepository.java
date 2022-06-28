package ua.foxminded.task10.uml.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>, CustomizedGroup {

    List<Group> findGroupsByNameOrderByName(String groupName);

    @Query("SELECT g FROM Group g ORDER BY g.name")
    List<Group> findAll();
}
