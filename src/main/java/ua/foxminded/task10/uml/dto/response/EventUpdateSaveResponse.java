package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.*;

import java.util.List;

@Data
public class EventUpdateSaveResponse {
    private EventDTO event;
    private List<SubjectDTO> subjects;
    private List<ClassroomDTO> classrooms;
    private List<TeacherDTO> teachers;
    private List<GroupDTO> groups;

    public EventUpdateSaveResponse(EventDTO event, List<SubjectDTO> subjects, List<ClassroomDTO> classrooms, List<TeacherDTO> teachers, List<GroupDTO> groups) {
        this.event = event;
        this.subjects = subjects;
        this.classrooms = classrooms;
        this.teachers = teachers;
        this.groups = groups;
    }
}
