package com.example.taskflow.service;


import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO) throws ResourceNotFoundException;
    UserDTO register(UserDTO userDTO) throws IllegalAccessException;
    UserDTO updateUser(Long userId, UserDTO userDTO);
    UserDTO findByEmail(String email) throws ResourceNotFoundException;
    TaskDTO SelfAssignTask(Long idUser, Long idTask) throws Exception;

    void deleteUser(Long userId);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);
}
