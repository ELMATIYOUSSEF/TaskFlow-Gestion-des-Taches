package com.example.taskflow.service;


import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Dto.response.UserVM;
import com.example.taskflow.Entity.Enums.TokenType;
import com.example.taskflow.Entity.TaskChangeRequest;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
@Service
public interface UserService {

    UserVM createUser(UserDTO userDTO) throws ResourceNotFoundException;
    UserDTO register(UserDTO userDTO) throws IllegalAccessException;
    UserVM updateUser(Long userId, UserDTO userDTO);
    UserDTO findByEmail(String email) throws ResourceNotFoundException;
    TaskDTO SelfAssignTask(Long idUser, Long idTask) throws Exception;

    String deleteUser(Long userId) throws ResourceNotFoundException;

    List<UserVM> getAllUsers();

    UserDTO getUserById(Long userId);
    Map<String, User> changeTask(Long adminUserId ,Long idTask , Long newUserId , Long lastUserId , Long newTaskId) throws Exception ;
    TaskChangeRequest demandChange(Long idUser, Long idTask, TokenType tokenType) throws ResourceNotFoundException, IllegalAccessException ;
    User DeleteTaskAssigned(Long idUser ,Long idTask) throws Exception;

    UserVM adminAssignTaskToUser(Long adminUserId, Long userIdToAssign, Long taskIdToAssign) throws Exception;

}
