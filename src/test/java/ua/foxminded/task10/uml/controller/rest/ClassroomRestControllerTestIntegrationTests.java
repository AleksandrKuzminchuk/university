package ua.foxminded.task10.uml.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.ClassroomCreateDTO;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.service.ClassroomService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ClassroomRestControllerTestIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClassroomService service;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        service.deleteAll();
    }

    @Test
    void givenStudentsDTOList_whenFindAll_thenReturnStudentsDTOList() throws Exception {

        createClassroomsDTO();

        ResultActions response = mockMvc.perform(get("/api/classrooms"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.classrooms.size()", is(2)));
    }

    @Test
    void givenClassroomDTOObject_whenCreateClassroomDTO_thenReturnSavedClassroomDTO() throws Exception {

        ClassroomCreateDTO classroomDTO = new ClassroomCreateDTO();
        classroomDTO.setNumber(45);

        ResultActions response = mockMvc.perform(post("/api/classrooms/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(classroomDTO)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number", is(classroomDTO.getNumber())));
    }

    @Test
    void givenClassroomDTOObject_whenCreateClassroomDTO_thenReturn400NotValid() throws Exception {

        ClassroomCreateDTO classroomDTO = new ClassroomCreateDTO();
        classroomDTO.setNumber(4564);

        ResultActions response = mockMvc.perform(post("/api/classrooms/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(classroomDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdatedClassroomDTOObject_whenUpdateClassroomDTO_thenReturnUpdatedClassroomDTOObject() throws Exception {

        ClassroomDTO savedClassroom = createClassroomDTO();

        ClassroomCreateDTO updateClassroom = new ClassroomCreateDTO();
        updateClassroom.setNumber(65);

        ResultActions response = mockMvc.perform(patch("/api/classrooms/update/{id}", savedClassroom.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateClassroom)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedClassroom.getId())))
                .andExpect(jsonPath("$.number", is(updateClassroom.getNumber())));
    }

    @Test
    void givenUpdatedClassroomDTOObject_whenUpdateClassroomDTO_thenReturn404NotFound() throws Exception {

        ClassroomCreateDTO updateClassroom = new ClassroomCreateDTO();
        updateClassroom.setNumber(65);

        ResultActions response = mockMvc.perform(patch("/api/classrooms/update/142")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateClassroom)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdatedClassroomDTOObject_whenUpdateClassroomDTO_thenReturn400NoValid() throws Exception {

        ClassroomDTO savedClassroom = createClassroomDTO();

        ClassroomCreateDTO updateClassroom = new ClassroomCreateDTO();
        updateClassroom.setNumber(6565);

        ResultActions response = mockMvc.perform(patch("/api/classrooms/update/{id}", savedClassroom.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateClassroom)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn200() throws Exception {

        ClassroomDTO classroomDTO = createClassroomDTO();

        ResultActions response = mockMvc.perform(delete("/api/classrooms/{id}/delete", classroomDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(delete("/api/classrooms/50/delete"));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClassroomDTOObject_whenFindByNumber_thenReturnClassroomObject() throws Exception {

        ClassroomDTO classroomDTO = createClassroomDTO();

        HttpHeaders headers = new HttpHeaders();
        headers.set("number", "45");

        ResultActions response = mockMvc.perform(get("/api/classrooms/find/by_number").
                contentType(MediaType.APPLICATION_JSON)
                .headers(headers));

        response.andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(classroomDTO.getId())))
                .andExpect(jsonPath("$.number", is(classroomDTO.getNumber())));
    }

    @Test
    void givenClassroomDTOObject_whenFindByNumber_thenReturn404NotFound() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("number", "85");

        ResultActions response = mockMvc.perform(get("/api/classrooms/find/by_number").
                contentType(MediaType.APPLICATION_JSON)
                .headers(headers));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteALl_thenReturn200() throws Exception {

        createClassroomsDTO();

        ResultActions response = mockMvc.perform(delete("/api/classrooms/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    public List<ClassroomDTO> createClassroomsDTO(){
        ClassroomDTO classroomTest = new ClassroomDTO();
        classroomTest.setNumber(45);
        ClassroomDTO classroomTest2 = new ClassroomDTO();
        classroomTest2.setNumber(5);
        List<ClassroomDTO> classroomDTOS = new ArrayList<>();
        classroomDTOS.add(classroomTest);
        classroomDTOS.add(classroomTest2);
        return classroomDTOS.stream()
                .map(classroomDTO -> service.save(classroomDTO)).collect(Collectors.toList());
    }

    private ClassroomDTO createClassroomDTO(){
        ClassroomDTO classroomTest = new ClassroomDTO();
        classroomTest.setNumber(45);
        return service.save(classroomTest);
    }
}