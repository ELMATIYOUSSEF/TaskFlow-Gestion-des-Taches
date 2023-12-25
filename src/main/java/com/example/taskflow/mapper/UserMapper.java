package com.example.taskflow.mapper;

import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//@Mapper(componentModel = "spring", uses = BCryptPasswordEncoder.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
  //  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mappings({
            @Mapping(source = "id", target = "userId"),
    })
    UserDTO entityToDto(User entity);

    @Mappings({
            @Mapping(source = "userId", target = "id"),
            @Mapping(source = "password", target = "password")
    })
    User dtoToEntity(UserDTO dto);


}
