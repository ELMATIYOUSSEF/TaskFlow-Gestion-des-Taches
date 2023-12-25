package com.example.taskflow.service;

import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.exception.ResourceNotFoundException;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    String Login(String email , String password , Model model) throws ResourceNotFoundException;
    String logout(String token) throws IllegalAccessException;
    UserDTO register(UserDTO userDTO) throws IllegalAccessException;
    UserDTO updateUser(Long userId, UserDTO userDTO);
    UserDTO findByEmail(String email) throws ResourceNotFoundException;

    void deleteUser(Long userId);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);
}
