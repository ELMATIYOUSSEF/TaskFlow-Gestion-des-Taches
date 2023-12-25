package com.example.taskflow.service.impl;


import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.mapper.TaskMapper;
import com.example.taskflow.mapper.UserMapper;
import com.example.taskflow.repository.UserRepository;
import com.example.taskflow.service.UserService;
import com.example.taskflow.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.dtoToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.entityToDto(savedUser);
    }

    public  UserDTO findByEmail(String email) throws ResourceNotFoundException {
       Optional<User> userOptional = userRepository.findByEmail(email);
       if(userOptional.isEmpty()) throw new ResourceNotFoundException("no User with this Email");
       return userMapper.entityToDto(userOptional.get());
    }
    @Override
    public String Login(String email , String password , Model model) throws ResourceNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            if (BCrypt.checkpw(password, userOptional.get().getPassword())) {
                model.addAttribute("user", userOptional.get());
                return jwtTokenUtil.generateToken(email);
            }
        }
        throw new ResourceNotFoundException("Invalid email or password");
    }

    public UserDTO register(UserDTO userDTO) throws IllegalAccessException {
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if(userOptional.isPresent()) throw new IllegalAccessException("this Email is already Exist ");
        User user = userMapper.dtoToEntity(userDTO);
       return userMapper.entityToDto(userRepository.save(user)) ;
    }

    @Override
    public String logout(String token) throws IllegalAccessException {
        if (token != null && token.startsWith("Bearer ")) {
            String authToken = token.substring(7);
            if (jwtTokenUtil.isTokenValid(authToken)) {
                return "Logged out successfully";
            }
        }
       throw new IllegalAccessException("Logout is Rejected");
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDTO.getUsername());
            //user.setRole(userDTO.getRole());
            //user.setTasks(userDTO.getTasks().stream().map(taskMapper::dtoToEntity).collect(Collectors.toList()));
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            User updatedUser = userRepository.save(user);
            return userMapper.entityToDto(updatedUser);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(userMapper::entityToDto).orElse(null);
    }

}
