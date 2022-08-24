package ua.foxminded.task10.uml.controller.mvc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.*;
import ua.foxminded.task10.uml.dto.response.EventUpdateSaveResponse;
import ua.foxminded.task10.uml.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.ConstantsTests.*;
import static ua.foxminded.task10.uml.util.MediaTypeTest.MEDIA_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = {"classpath:create-table-classrooms.sql", "classpath:create-table-subjects.sql", "classpath:create-table-teachers_subjects.sql",
        "classpath:create-table-teachers.sql", "classpath:create-table-groups.sql", "classpath:create-table-events.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EventControllerIntegrationTests {

    @Autowired
    private EventService service;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service.deleteAll();
        classroomService.deleteAll();
        teacherService.deleteAll();
        subjectService.deleteAll();
        groupService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        service.deleteAll();
        classroomService.deleteAll();
        teacherService.deleteAll();
        subjectService.deleteAll();
        groupService.deleteAll();
    }

    @Test
    void shouldReturnEventsDTOList_whenFindAll() throws Exception {

        List<EventDTO> eventsDTO = createEventsDTO();

        ResultActions response = mockMvc.perform(get("/events"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("events", hasSize(2)))
                .andExpect(model().attribute("events", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("dateTime", is(eventsDTO.get(0).getDateTime())),
                                hasProperty("group", is(eventsDTO.get(0).getGroup())),
                                hasProperty("classroom", is(eventsDTO.get(0).getClassroom())),
                                hasProperty("subject", is(eventsDTO.get(0).getSubject())),
                                hasProperty("teacher", is(eventsDTO.get(0).getTeacher()))
                        )
                )))
                .andExpect(model().attribute("events", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("dateTime", is(eventsDTO.get(1).getDateTime())),
                                hasProperty("group", is(eventsDTO.get(1).getGroup())),
                                hasProperty("classroom", is(eventsDTO.get(1).getClassroom())),
                                hasProperty("subject", is(eventsDTO.get(1).getSubject())),
                                hasProperty("teacher", is(eventsDTO.get(1).getTeacher()))
                        )
                )))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("events/events"));
    }

    @Test
    void shouldReturnSaveForm() throws Exception {

        EventUpdateSaveResponse eventUpdateSaveResponse = service.saveForm();

        ResultActions response = mockMvc.perform(get("/events/new"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("saveEvent", eventUpdateSaveResponse))
                .andExpect(view().name("events/formSaveEvent"));
    }

    @Test
    void shouldReturnSavedEventDTO_whenSave() throws Exception {

        EventDTO saveEventDTO = createEventDTO();

        ResultActions response = mockMvc.perform(post("/events/saved")
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(saveEventDTO.getDateTime()))
                .param("group.id", String.valueOf(saveEventDTO.getGroup().getId()))
                .param("classroom.id", String.valueOf(saveEventDTO.getClassroom().getId()))
                .param("subject.id", String.valueOf(saveEventDTO.getSubject().getId()))
                .param("teacher.id", String.valueOf(saveEventDTO.getTeacher().getId()))
                .sessionAttr("saveEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("events/fromSavedEvent"));
    }

    @Test
    void shouldReturn404NotFoundSubject_whenSave() throws Exception {

        EventDTO saveEventDTO = createEventDTO();

        ResultActions response = mockMvc.perform(post("/events/saved")
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(saveEventDTO.getDateTime()))
                .param("group.id", String.valueOf(saveEventDTO.getGroup().getId()))
                .param("classroom.id", String.valueOf(saveEventDTO.getClassroom().getId()))
                .param("subject.id", String.valueOf(ID_NOT_EXISTS))
                .param("teacher.id", String.valueOf(saveEventDTO.getTeacher().getId()))
                .sessionAttr("saveEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundClassroom_whenSave() throws Exception {

        EventDTO saveEventDTO = createEventDTO();

        ResultActions response = mockMvc.perform(post("/events/saved")
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(saveEventDTO.getDateTime()))
                .param("group.id", String.valueOf(saveEventDTO.getGroup().getId()))
                .param("classroom.id", String.valueOf(ID_NOT_EXISTS))
                .param("subject.id", String.valueOf(saveEventDTO.getSubject().getId()))
                .param("teacher.id", String.valueOf(saveEventDTO.getTeacher().getId()))
                .sessionAttr("saveEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundGroup_whenSave() throws Exception {

        EventDTO saveEventDTO = createEventDTO();

        ResultActions response = mockMvc.perform(post("/events/saved")
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(saveEventDTO.getDateTime()))
                .param("group.id", String.valueOf(ID_NOT_EXISTS))
                .param("classroom.id", String.valueOf(saveEventDTO.getClassroom().getId()))
                .param("subject.id", String.valueOf(saveEventDTO.getSubject().getId()))
                .param("teacher.id", String.valueOf(saveEventDTO.getTeacher().getId()))
                .sessionAttr("saveEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenSave() throws Exception {

        EventDTO saveEventDTO = createEventDTO();

        ResultActions response = mockMvc.perform(post("/events/saved")
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(saveEventDTO.getDateTime()))
                .param("group.id", String.valueOf(saveEventDTO.getGroup().getId()))
                .param("classroom.id", String.valueOf(saveEventDTO.getClassroom().getId()))
                .param("subject.id", String.valueOf(saveEventDTO.getSubject().getId()))
                .param("teacher.id", String.valueOf(ID_NOT_EXISTS))
                .sessionAttr("saveEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdateForm() throws Exception {

        EventDTO eventDTO = createSaveEventDTO();

        EventUpdateSaveResponse eventUpdateSaveResponse = service.updateForm(eventDTO.getId());

        ResultActions response = mockMvc.perform(get("/events/{id}/update", eventDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("updateEvent", eventUpdateSaveResponse))
                .andExpect(view().name("events/formUpdateEvent"));
    }

    @Test
    void shouldReturn404NotFound_whenUpdateForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/events/{id}/update", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedEventDTO_whenUpdate() throws Exception {

        EventDTO eventDTO = createSaveEventDTO();

        EventDTO updateEventDTO = createUpdateEventDTO();

        ResultActions response = mockMvc.perform(patch("/events/{id}/updated", eventDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(updateEventDTO.getDateTime()))
                .param("subject.id", String.valueOf(updateEventDTO.getSubject().getId()))
                .param("classroom.id", String.valueOf(updateEventDTO.getClassroom().getId()))
                .param("group.id", String.valueOf(updateEventDTO.getGroup().getId()))
                .param("teacher.id", String.valueOf(updateEventDTO.getTeacher().getId()))
                .sessionAttr("updateEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("event", hasProperty("id", is(eventDTO.getId()))))
                .andExpect(model().attribute("event", hasProperty("dateTime", is(updateEventDTO.getDateTime()))))
                .andExpect(model().attribute("event", hasProperty("subject", is(new SubjectDTO(2)))))
                .andExpect(model().attribute("event", hasProperty("classroom", is(new ClassroomDTO(2)))))
                .andExpect(model().attribute("event", hasProperty("group", is(new GroupDTO(2)))))
                .andExpect(model().attribute("event", hasProperty("teacher", is(new TeacherDTO(2)))))
                .andExpect(view().name("events/formUpdatedEvent"));
    }

    @Test
    void shouldReturnNotFound_whenUpdate() throws Exception {

        ResultActions response = mockMvc.perform(patch("/events/{id}/updated", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundSubject_whenUpdate() throws Exception {

        EventDTO eventDTO = createSaveEventDTO();

        EventDTO updateEventDTO = createUpdateEventDTO();

        ResultActions response = mockMvc.perform(patch("/events/{id}/updated", eventDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(updateEventDTO.getDateTime()))
                .param("subject.id", String.valueOf(ID_NOT_EXISTS))
                .param("classroom.id", String.valueOf(updateEventDTO.getClassroom().getId()))
                .param("group.id", String.valueOf(updateEventDTO.getGroup().getId()))
                .param("teacher.id", String.valueOf(updateEventDTO.getTeacher().getId()))
                .sessionAttr("updateEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundClassroom_whenUpdate() throws Exception {

        EventDTO eventDTO = createSaveEventDTO();

        EventDTO updateEventDTO = createUpdateEventDTO();

        ResultActions response = mockMvc.perform(patch("/events/{id}/updated", eventDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(updateEventDTO.getDateTime()))
                .param("subject.id", String.valueOf(updateEventDTO.getSubject().getId()))
                .param("classroom.id", String.valueOf(ID_NOT_EXISTS))
                .param("group.id", String.valueOf(updateEventDTO.getGroup().getId()))
                .param("teacher.id", String.valueOf(updateEventDTO.getTeacher().getId()))
                .sessionAttr("updateEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundGroup_whenUpdate() throws Exception {

        EventDTO eventDTO = createSaveEventDTO();

        EventDTO updateEventDTO = createUpdateEventDTO();

        ResultActions response = mockMvc.perform(patch("/events/{id}/updated", eventDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(updateEventDTO.getDateTime()))
                .param("subject.id", String.valueOf(updateEventDTO.getSubject().getId()))
                .param("classroom.id", String.valueOf(updateEventDTO.getClassroom().getId()))
                .param("group.id", String.valueOf(ID_NOT_EXISTS))
                .param("teacher.id", String.valueOf(updateEventDTO.getTeacher().getId()))
                .sessionAttr("updateEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenUpdate() throws Exception {

        EventDTO eventDTO = createSaveEventDTO();

        EventDTO updateEventDTO = createUpdateEventDTO();

        ResultActions response = mockMvc.perform(patch("/events/{id}/updated", eventDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("dateTime", String.valueOf(updateEventDTO.getDateTime()))
                .param("subject.id", String.valueOf(updateEventDTO.getSubject().getId()))
                .param("classroom.id", String.valueOf(updateEventDTO.getClassroom().getId()))
                .param("group.id", String.valueOf(updateEventDTO.getGroup().getId()))
                .param("teacher.id", String.valueOf(ID_NOT_EXISTS))
                .sessionAttr("updateEvent", EventDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteById() throws Exception {

        EventDTO eventDTO = createSaveEventDTO();

        ResultActions response = mockMvc.perform(delete("/events/{id}/deleted", eventDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("events/formDeletedEvent"));
    }

    @Test
    void shouldReturn404NotFound_whenDeleteById() throws Exception {

        ResultActions response = mockMvc.perform(delete("/events/{id}/deleted", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFindEventsForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/events/find")
                .sessionAttr("event", EventExtraDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("events/formForFindEvents"));
    }

    @Test
    void shouldReturnEventsDTOList_whenFindEvents() throws Exception {

        List<EventDTO> eventsDTO = createEventsDTO();

        ResultActions response = mockMvc.perform(get("/events/found")
                .contentType(MEDIA_TYPE)
                .param("startDateTime", String.valueOf(START_DATE_TIME))
                .param("endDateTime", String.valueOf(END_DATE_TIME))
                .sessionAttr("findEvents", EventExtraDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("events", hasSize(2)))
                .andExpect(model().attribute("events", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("dateTime", is(eventsDTO.get(0).getDateTime())),
                                hasProperty("group", is(eventsDTO.get(0).getGroup())),
                                hasProperty("classroom", is(eventsDTO.get(0).getClassroom())),
                                hasProperty("subject", is(eventsDTO.get(0).getSubject())),
                                hasProperty("teacher", is(eventsDTO.get(0).getTeacher()))
                        )
                )))
                .andExpect(model().attribute("events", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("dateTime", is(eventsDTO.get(1).getDateTime())),
                                hasProperty("group", is(eventsDTO.get(1).getGroup())),
                                hasProperty("classroom", is(eventsDTO.get(1).getClassroom())),
                                hasProperty("subject", is(eventsDTO.get(1).getSubject())),
                                hasProperty("teacher", is(eventsDTO.get(1).getTeacher()))
                        )
                )))
                .andExpect(model().attribute("event", hasProperty("startDateTime", is(START_DATE_TIME))))
                .andExpect(model().attribute("event", hasProperty("endDateTime", is(END_DATE_TIME))))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("events/formFoundEvents"));
    }

    @Test
    void shouldReturn200_whenDeleteAll() throws Exception {

        createEventsDTO();

        ResultActions response = mockMvc.perform(delete("/events/deleted/all"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("events/formDeleteAllEvents"));
    }

    private SubjectDTO createSubjectDTO() {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("GEOMETRY");
        return subjectService.save(subjectDTO);
    }

    private GroupDTO createGroupDTO() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("G-19");
        return groupService.save(groupDTO);
    }

    private ClassroomDTO createClassroomDTO() {
        ClassroomDTO classroomDTO = new ClassroomDTO();
        classroomDTO.setNumber(455);
        return classroomService.save(classroomDTO);
    }

    private TeacherDTO createTeacherDTO() {
        return teacherService.save(new TeacherDTO("Hurmek", "Fekir"));
    }

    private EventDTO createSaveEventDTO() {
        return service.save(new EventDTO(GENERATE_DATE_TIME, createSubjectDTO(),
                createClassroomDTO(), createGroupDTO(), createTeacherDTO()));
    }

    private ClassroomDTO createUpdateClassroomsDTO() {
        ClassroomDTO classroomTest = new ClassroomDTO();
        classroomTest.setNumber(457);
        return classroomService.save(classroomTest);
    }

    private GroupDTO createUpdateGroupsDTO() {
        GroupDTO groupDTOTest = new GroupDTO();
        groupDTOTest.setName("G-57");
        return groupService.save(groupDTOTest);
    }

    private TeacherDTO createUpdateTeachersDTO() {
        return teacherService.save(new TeacherDTO("Karl", "Markovich"));
    }

    private SubjectDTO createUpdateSubjectsDTO() {
        return subjectService.save(new SubjectDTO("CHEMISTRY"));
    }

    private List<EventDTO> createEventsDTO() {
        List<EventDTO> eventsDTO = new ArrayList<>();
        SubjectDTO subjectDTO = createSubjectDTO();
        ClassroomDTO classroomDTO = createClassroomDTO();
        GroupDTO groupDTO = createGroupDTO();
        TeacherDTO teacherDTO = createTeacherDTO();
        ClassroomDTO classroomDTO1 = new ClassroomDTO();
        classroomDTO1.setNumber(78);
        GroupDTO groupDTO1 = new GroupDTO();
        groupDTO1.setName("G-67");
        eventsDTO.add(new EventDTO(GENERATE_DATE_TIME, subjectDTO,
                classroomDTO, groupDTO, teacherDTO));
        eventsDTO.add(new EventDTO(GENERATE_DATE_TIME, subjectDTO,
                classroomDTO, groupDTO, teacherDTO));
        return eventsDTO.stream().map(eventDTO -> service.save(eventDTO)).collect(Collectors.toList());
    }

    private EventDTO createEventDTO() {
        SubjectDTO subjectDTO = createSubjectDTO();
        ClassroomDTO classroomDTO = createClassroomDTO();
        GroupDTO groupDTO = createGroupDTO();
        TeacherDTO teacherDTO = createTeacherDTO();
        ClassroomDTO classroomDTO1 = new ClassroomDTO();
        classroomDTO1.setNumber(78);
        GroupDTO groupDTO1 = new GroupDTO();
        groupDTO1.setName("G-67");
        return new EventDTO(GENERATE_DATE_TIME, subjectDTO,
                classroomDTO, groupDTO, teacherDTO);
    }

    private EventDTO createUpdateEventDTO() {
        return new EventDTO(GENERATE_DATE_TIME, createUpdateSubjectsDTO(), createUpdateClassroomsDTO(),
                createUpdateGroupsDTO(), createUpdateTeachersDTO());
    }
}