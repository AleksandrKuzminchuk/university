package ua.foxminded.task10.uml.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("FROM Event e WHERE e.dateTime BETWEEN :startDateTime AND :endDateTime")
    List<Event> findByDateTimeOrderByDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
