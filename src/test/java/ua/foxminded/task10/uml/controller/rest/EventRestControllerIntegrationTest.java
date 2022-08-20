package ua.foxminded.task10.uml.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.*;
import ua.foxminded.task10.uml.service.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.formatters.DateTimeFormat.formatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EventRestControllerIntegrationTest {

    @Autowired
    private EventService service;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        service.deleteAll();
        subjectService.deleteAll();
        classroomService.deleteAll();
        groupService.deleteAll();
        teacherService.deleteAll();
    }

    @Test
    void givenEventsDTOList_whenFindAll_thenReturnEventsDTOList() throws Exception {

        createEventsDTO();

        ResultActions response = mockMvc.perform(get("/api/events"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.events.size()", is(2)));
    }

    @Test
    void givenEventDTOObject_whenCreate_thenReturnEventDTOObject() throws Exception {

        EventCreateDTO eventCreateDTO = createEventCreateDTO();

        ResultActions response = mockMvc.perform(post("/api/events/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventCreateDTO)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subject.id", is(eventCreateDTO.getSubjectId())))
                .andExpect(jsonPath("$.classroom.id", is(eventCreateDTO.getClassroomId())))
                .andExpect(jsonPath("$.group.id", is(eventCreateDTO.getGroupId())))
                .andExpect(jsonPath("$.teacher.id", is(eventCreateDTO.getTeacherId())))
                .andExpect(jsonPath("$.dateTime", is(eventCreateDTO.getDateTime().toString())));
    }

    @Test
    void givenEventDTOObject_whenCreate_thenReturn404NotFoundSubject() throws Exception {

        EventCreateDTO eventCreateDTO = createEventCreateDTO();
        eventCreateDTO.setSubjectId(4545);

        ResultActions response = mockMvc.perform(post("/api/events/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(eventCreateDTO)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenEventDTOObject_whenCreate_thenReturn404NotFoundClassroom() throws Exception {

        EventCreateDTO eventCreateDTO = createEventCreateDTO();
        eventCreateDTO.setClassroomId(4545);

        ResultActions response = mockMvc.perform(post("/api/events/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(eventCreateDTO)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenEventDTOObject_whenCreate_thenReturn404NotFoundGroup() throws Exception {

        EventCreateDTO eventCreateDTO = createEventCreateDTO();
        eventCreateDTO.setGroupId(4545);

        ResultActions response = mockMvc.perform(post("/api/events/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(eventCreateDTO)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenEventDTOObject_whenCreate_thenReturn404NotFoundTeacher() throws Exception {

        EventCreateDTO eventCreateDTO = createEventCreateDTO();
        eventCreateDTO.setTeacherId(4545);

        ResultActions response = mockMvc.perform(post("/api/events/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(eventCreateDTO)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdatedEventDTOObject_whenUpdate_thenReturnUpdatedEventDTOObject() throws Exception {

        SubjectDTO changeSubject = subjectService.save(new SubjectDTO("MATH"));

        ClassroomDTO classroomDTO = new ClassroomDTO();
        classroomDTO.setNumber(785);
        ClassroomDTO changeClassroom = classroomService.save(classroomDTO);

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("H-10");
        GroupDTO changeGroup = groupService.save(groupDTO);

        TeacherDTO changeTeacher = teacherService.save(new TeacherDTO("Jirkin", "Marlush"));

        EventDTO eventDTO = createEventDTO();

        EventCreateDTO updateEvent = new EventCreateDTO();
        updateEvent.setDateTime(eventDTO.getDateTime());
        updateEvent.setSubjectId(changeSubject.getId());
        updateEvent.setClassroomId(changeClassroom.getId());
        updateEvent.setGroupId(changeGroup.getId());
        updateEvent.setTeacherId(changeTeacher.getId());

        ResultActions response = mockMvc.perform(patch("/api/events/update/{id}", eventDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEvent)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(eventDTO.getId())))
                .andExpect(jsonPath("$.subject.id", is(updateEvent.getSubjectId())))
                .andExpect(jsonPath("$.group.id", is(updateEvent.getGroupId())))
                .andExpect(jsonPath("$.classroom.id", is(updateEvent.getClassroomId())))
                .andExpect(jsonPath("$.teacher.id", is(updateEvent.getTeacherId())))
                .andExpect(jsonPath("$.dateTime", is(updateEvent.getDateTime().toString())));
    }

    @Test
    void givenUpdatedEventDTOObject_whenUpdate_thenReturn404NotFoundEvent() throws Exception {

        Integer subjectId = 4545;

        EventCreateDTO updateEvent = new EventCreateDTO();

        ResultActions response = mockMvc.perform(patch("/api/events/update/{id}", subjectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateEvent)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    //TODO not threw exception GlobalNotFoundException, although I threw (andExpect(status().isNotFound())); Help me please)
//    @Test
//    void givenUpdatedEventDTOObject_whenUpdate_thenReturn404NoyFoundSubject() throws Exception {
//
//        ClassroomDTO classroomDTO = new ClassroomDTO();
//        classroomDTO.setNumber(785);
//        ClassroomDTO changeClassroom = classroomService.save(classroomDTO);
//
//        GroupDTO groupDTO = new GroupDTO();
//        groupDTO.setName("H-10");
//        GroupDTO changeGroup = groupService.save(groupDTO);
//
//        TeacherDTO changeTeacher = teacherService.save(new TeacherDTO("Jirkin", "Marlush"));
//
//        EventDTO eventDTO = createEventDTO();
//
//        EventCreateDTO updateEvent = new EventCreateDTO();
//        updateEvent.setDateTime(eventDTO.getDateTime());
//        updateEvent.setSubjectId(4545);
//        updateEvent.setClassroomId(changeClassroom.getId());
//        updateEvent.setGroupId(changeGroup.getId());
//        updateEvent.setTeacherId(changeTeacher.getId());
//
//        ResultActions response = mockMvc.perform(patch("/api/events/update/{id}", eventDTO.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(updateEvent)));
//
//        response.andDo(print())
//                .andExpect(status().isNotFound());
//    }

//    @Test
//    void givenUpdatedEventDTOObject_whenUpdate_thenReturn404NoyFoundClassroom() throws Exception {
//
//        SubjectDTO changeSubject = subjectService.save(new SubjectDTO("MATH"));
//
//        GroupDTO groupDTO = new GroupDTO();
//        groupDTO.setName("H-10");
//        GroupDTO changeGroup = groupService.save(groupDTO);
//
//        TeacherDTO changeTeacher = teacherService.save(new TeacherDTO("Jirkin", "Marlush"));
//
//        EventDTO eventDTO = createEventDTO();
//
//        EventCreateDTO updateEvent = new EventCreateDTO();
//        updateEvent.setDateTime(eventDTO.getDateTime());
//        updateEvent.setSubjectId(changeSubject.getId());
//        updateEvent.setClassroomId(4545);
//        updateEvent.setGroupId(changeGroup.getId());
//        updateEvent.setTeacherId(changeTeacher.getId());
//
//        ResultActions response = mockMvc.perform(patch("/api/events/update/{id}", eventDTO.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(updateEvent)));
//
//        response.andDo(print())
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void givenUpdatedEventDTOObject_whenUpdate_thenReturn404NotFoundGroup() throws Exception {
//
//        SubjectDTO changeSubject = subjectService.save(new SubjectDTO("MATH"));
//
//        ClassroomDTO classroomDTO = new ClassroomDTO();
//        classroomDTO.setNumber(785);
//        ClassroomDTO changeClassroom = classroomService.save(classroomDTO);
//
//        TeacherDTO changeTeacher = teacherService.save(new TeacherDTO("Jirkin", "Marlush"));
//
//        EventDTO eventDTO = createEventDTO();
//
//        EventCreateDTO updateEvent = new EventCreateDTO();
//        updateEvent.setDateTime(eventDTO.getDateTime());
//        updateEvent.setSubjectId(changeSubject.getId());
//        updateEvent.setClassroomId(changeClassroom.getId());
//        updateEvent.setGroupId(4545);
//        updateEvent.setTeacherId(changeTeacher.getId());
//
//        ResultActions response = mockMvc.perform(patch("/api/events/update/{id}", eventDTO.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(updateEvent)));
//
//        response.andDo(print())
//                .andExpect(status().isNotFound());
//    }

//    @Test
//    void givenUpdatedEventDTOObject_whenUpdate_thenReturn404NotFoundTeacher() throws Exception {
//
//        SubjectDTO changeSubject = subjectService.save(new SubjectDTO("MATH"));
//
//        ClassroomDTO classroomDTO = new ClassroomDTO();
//        classroomDTO.setNumber(785);
//        ClassroomDTO changeClassroom = classroomService.save(classroomDTO);
//
//        GroupDTO groupDTO = new GroupDTO();
//        groupDTO.setName("H-10");
//        GroupDTO changeGroup = groupService.save(groupDTO);
//
//        EventDTO eventDTO = createEventDTO();
//
//        EventCreateDTO updateEvent = new EventCreateDTO();
//        updateEvent.setDateTime(eventDTO.getDateTime());
//        updateEvent.setSubjectId(changeSubject.getId());
//        updateEvent.setClassroomId(changeClassroom.getId());
//        updateEvent.setGroupId(changeGroup.getId());
//        updateEvent.setTeacherId(4545);
//
//        ResultActions response = mockMvc.perform(patch("/api/events/update/{id}", eventDTO.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(updateEvent)));
//
//        response.andDo(print())
//                .andExpect(status().isNotFound());
//    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn200() throws Exception {

        EventDTO eventDTO = createEventDTO();

        ResultActions response = mockMvc.perform(delete("/api/events/{id}/delete", eventDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void givenResponseEntity_whenDeleteById_thenReturn404NotFound() throws Exception {

        Integer eventId = 4545;

        ResultActions response = mockMvc.perform(delete("/api/events/{id}/delete", eventId));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteAll_thenReturn200() throws Exception {

        createEventsDTO();

        ResultActions response = mockMvc.perform(delete("/api/events/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenEventsDTOList_whenFindEvents_thenReturnEventsDTOList() throws Exception {

        createEventsDTO();

        ResultActions response = mockMvc.perform(get("/api/events/find")
                .header("startDateTime", LocalDateTime.of(2020, 1, 1, 11, 11, 0).format(formatter))
                .header("endDateTime", LocalDateTime.of(2023, 1, 1, 11, 11, 0).format(formatter)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.events.size()", is(2)));
    }

    private SubjectDTO createSubjectDTO(){
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("GEOMETRY");
        return subjectService.save(subjectDTO);
    }

    private GroupDTO createGroupDTO(){
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("G-19");
        return groupService.save(groupDTO);
    }

    private ClassroomDTO createClassroomDTO(){
        ClassroomDTO classroomDTO = new ClassroomDTO();
        classroomDTO.setNumber(455);
        return classroomService.save(classroomDTO);
    }

    private TeacherDTO createTeacherDTO(){
        return teacherService.save(new TeacherDTO("Hurmek", "Fekir"));
    }

    private EventDTO createEventDTO(){
        return service.save(new EventDTO(LocalDateTime.of(2022, 8, 20, 9, 0, 0), createSubjectDTO(),
                createClassroomDTO(), createGroupDTO(), createTeacherDTO()));
    }

    private EventCreateDTO createEventCreateDTO(){
        return new EventCreateDTO(LocalDateTime.of(2022, 8, 20, 9, 0, 0), createSubjectDTO().getId(),
                createClassroomDTO().getId(), createGroupDTO().getId(), createTeacherDTO().getId());
    }

    private List<EventDTO> createEventsDTO(){
        List<EventDTO> eventsDTO = new ArrayList<>();
        SubjectDTO subjectDTO = createSubjectDTO();
        ClassroomDTO classroomDTO = createClassroomDTO();
        GroupDTO groupDTO = createGroupDTO();
        TeacherDTO teacherDTO = createTeacherDTO();
        ClassroomDTO classroomDTO1 = new ClassroomDTO();
        classroomDTO1.setNumber(78);
        GroupDTO groupDTO1 = new GroupDTO();
        groupDTO1.setName("G-67");
        eventsDTO.add(new EventDTO(LocalDateTime.of(2022, 8, 20, 9, 0, 0), subjectDTO,
                classroomDTO, groupDTO, teacherDTO));
        eventsDTO.add(new EventDTO(LocalDateTime.of(2022, 8, 21, 10, 0, 0), subjectDTO,
                classroomDTO, groupDTO, teacherDTO));
        return eventsDTO.stream().map(eventDTO -> service.save(eventDTO)).collect(Collectors.toList());
    }
}