package ua.foxminded.task10.uml.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.model.Group;

@Slf4j
@Component
public class GroupMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public GroupMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Group convertToGroup(GroupDTO groupDTO){
        log.info("Mapping to group...");
        return modelMapper.map(groupDTO, Group.class);
    }

    public GroupDTO convertToGroupDTO(Group group){
        log.info("Mapping to groupDTO...");
        return modelMapper.map(group, GroupDTO.class);
    }
}
