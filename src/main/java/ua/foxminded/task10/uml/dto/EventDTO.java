package ua.foxminded.task10.uml.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EventDTO")
public class EventDTO {

    @ApiModelProperty(notes = "Unique Id event", example = "23", allowEmptyValue = true, position = 1)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(notes = "Date and time event", example = "2022-08-02 11:11", position = 2)
    private LocalDateTime dateTime;

    @ApiModelProperty(notes = "Subject unique id and name", example = "subject\n{\nid: 21\nname: MATH\n}", position = 3)
    private SubjectDTO subject;
    @ApiModelProperty(notes = "Classroom unique id and number", example = "classroom\n{\nid: 51\nname: 25\n}", position = 4)
    private ClassroomDTO classroom;
    @ApiModelProperty(notes = "Group unique id and name", example = "group\n{\nid: 45\nname: G-10\n}", position = 5)
    private GroupDTO group;
    @ApiModelProperty(notes = "Teacher name and surname", example = "teacher\n{\nid: 21\nfirstName: Kiril\nlastName: Hurmek\n}", position = 6)
    private TeacherDTO teacher;

    public EventDTO(LocalDateTime dateTime, SubjectDTO subject, ClassroomDTO classroom, GroupDTO group, TeacherDTO teacher) {
        this.dateTime = dateTime;
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
    }
}
