package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.EventDTO;

import java.util.List;

@Data
public class EventResponse {

    private List<EventDTO> events;

    public EventResponse(List<EventDTO> events) {
        this.events = events;
    }
}
