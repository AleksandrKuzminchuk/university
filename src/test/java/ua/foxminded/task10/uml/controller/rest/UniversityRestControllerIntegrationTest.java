package ua.foxminded.task10.uml.controller.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.*;
import ua.foxminded.task10.uml.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.formatters.DateTimeFormat.formatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = {"classpath:create-table-classrooms.sql", "classpath:create-table-subjects.sql", "classpath:create-table-teachers_subjects.sql",
        "classpath:create-table-teachers.sql", "classpath:create-table-groups.sql", "classpath:create-table-events.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UniversityRestControllerIntegrationTest {

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

    @BeforeEach
    void setUp() {
        service.deleteAll();
        subjectService.deleteAll();
        classroomService.deleteAll();
        groupService.deleteAll();
        teacherService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        service.deleteAll();
        subjectService.deleteAll();
        classroomService.deleteAll();
        groupService.deleteAll();
        teacherService.deleteAll();
    }

    @Test
    void givenEventsDTOList_whenShoeUniversity_thenReturnEventsDTOList() throws Exception {

        createEventsDTO();

        ResultActions response = mockMvc.perform(get("/api/university"));

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


    private List<EventDTO> createEventsDTO(){
        List<EventDTO> eventsDTO = new ArrayList<>();
        SubjectDTO subjectDTO = createSubjectDTO();
        ClassroomDTO classroomDTO = createClassroomDTO();
        GroupDTO groupDTO = createGroupDTO();
        TeacherDTO teacherDTO = createTeacherDTO();
        GroupDTO groupDTO1 = new GroupDTO();
        groupDTO1.setName("G-67");
        eventsDTO.add(new EventDTO(LocalDateTime.now(), subjectDTO,
                classroomDTO, groupDTO, teacherDTO));
        eventsDTO.add(new EventDTO(LocalDateTime.now(), subjectDTO,
                classroomDTO, groupDTO, teacherDTO));
        return eventsDTO.stream().map(eventDTO -> service.save(eventDTO)).collect(Collectors.toList());
    }
}