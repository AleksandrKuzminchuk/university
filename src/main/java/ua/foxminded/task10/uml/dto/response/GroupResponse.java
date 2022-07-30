package ua.foxminded.task10.uml.dto.response;

import lombok.Data;
import ua.foxminded.task10.uml.dto.GroupDTO;

import java.util.List;

@Data
public class GroupResponse {

    private List<GroupDTO> groups;

    public GroupResponse(List<GroupDTO> groups) {
        this.groups = groups;
    }
}
