package com.example.taskflow.mapper;

import com.example.taskflow.Dto.TagDTO;
import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Entity.Tag;
import com.example.taskflow.Entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "taskId")
    })
    TaskDTO entityToDto(Task entity);

    @Mappings({
            @Mapping(source = "taskId", target = "id")
    })
    Task dtoToEntity(TaskDTO dto);

}
