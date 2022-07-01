package ua.foxminded.task10.uml.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
