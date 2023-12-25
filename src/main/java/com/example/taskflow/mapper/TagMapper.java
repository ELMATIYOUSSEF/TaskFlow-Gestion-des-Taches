package com.example.taskflow.mapper;

import com.example.taskflow.Dto.TagDTO;
import com.example.taskflow.Entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    @Mappings({
            @Mapping(target = "tagId", source = "id")
    })
    TagDTO entityToDto(Tag entity);

    @Mappings({
            @Mapping(target = "id", source = "tagId")
    })
    Tag dtoToEntity(TagDTO dto);

}
