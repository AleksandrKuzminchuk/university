package ua.foxminded.task10.uml.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.classroom " +
            "LEFT JOIN FETCH e.subject " +
            "LEFT JOIN FETCH e.teacher " +
            "LEFT JOIN FETCH e.group" +
            " WHERE e.id=:eventId " +
            "ORDER BY e.dateTime")
    Optional<Event> findById(@Param("eventId") @NonNull Integer eventId);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.classroom " +
            "LEFT JOIN FETCH e.subject " +
            "LEFT JOIN FETCH e.teacher " +
            "LEFT JOIN FETCH e.group " +
            "ORDER BY e.dateTime")
    List<Event> findAll();

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.classroom " +
            "LEFT JOIN FETCH e.subject " +
            "LEFT JOIN FETCH e.teacher " +
            "LEFT JOIN FETCH e.group " +
            "WHERE e.dateTime BETWEEN :from AND :to " +
            "ORDER BY e.dateTime")
    List<Event> findEvents(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
