package ua.foxminded.task10.uml.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ApiModel(value = "EventCreateDTO")
public class EventCreateDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(notes = "Date and time event", example = "2022-08-02T11:11", position = 1)
    private LocalDateTime dateTime;

    @ApiModelProperty(notes = "Subject Id", example = "33", position = 2)
    private Integer subjectId;
    @ApiModelProperty(notes = "Classroom Id", example = "14", position = 3)
    private Integer classroomId;
    @ApiModelProperty(notes = "Group Id", example = "1", position = 4)
    private Integer groupId;
    @ApiModelProperty(notes = "Teacher Id", example = "51", position = 5)
    private Integer teacherId;
}
