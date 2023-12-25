package com.example.taskflow.service.impl;


import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Entity.Enums.StatusRequest;
import com.example.taskflow.Entity.Enums.TokenType;
import com.example.taskflow.Entity.Task;
import com.example.taskflow.Entity.TaskChangeRequest;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.mapper.TaskMapper;
import com.example.taskflow.mapper.UserMapper;
import com.example.taskflow.repository.UserRepository;
import com.example.taskflow.security.UserAuth;
import com.example.taskflow.service.TaskChangeRequestService;
import com.example.taskflow.service.TaskService;
import com.example.taskflow.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final UserAuth userAuth;
    private final TaskChangeRequestService taskChangeRequestService;

    @Override
    public UserDTO createUser(UserDTO userDTO) throws ResourceNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if(userOptional.isPresent()) throw new ResourceNotFoundException("This Email Is Already Exist !");
        return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(userDTO)));
    }

    public  UserDTO findByEmail(String email) throws ResourceNotFoundException {
       Optional<User> userOptional = userRepository.findByEmail(email);
       if(userOptional.isEmpty()) throw new ResourceNotFoundException("No User with this Email");
       return userMapper.entityToDto(userOptional.get());
    }

    public String login(String email, String password) throws ResourceNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check the password using BCrypt
            if (BCrypt.checkpw(password, user.getPassword())) {
                return "Login successful";
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

    public String logout(String token) throws IllegalAccessException {
        if (token != null && token.startsWith("Bearer ")) {
            String authToken = token.substring(7);
                return "Logged out successfully";
            }
       throw new IllegalAccessException("Logout is Rejected");
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDTO.getUsername());
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

    public TaskDTO SelfAssignTask(Long idUser, Long idTask) throws Exception {
        Task task = taskService.getTaskById(idTask);
       if( task.getExpDate().isBefore(LocalDateTime.now())) throw new IllegalArgumentException("this Task is Completed");
       Optional<User> user = userRepository.findById(idUser);
        if(user.isPresent() && user.get().equals(userAuth.generateUser())){
            task.setAssignedDate(LocalDateTime.now().plusDays(3));
            task.setUser(user.get());
          return taskService.createTask(taskMapper.entityToDto(task));
        }
        throw new IllegalArgumentException("you have not access to assign this Task for authors ");
    }
    @Transactional
    public Map<String, User> changeTask(Long idTask , Long newUserId , Long lastUserId , Long newTaskId) throws Exception {
        Map<String, User> result = new HashMap<>();
        User newUser = userRepository.findById(newUserId).orElseThrow(()-> new ResourceNotFoundException("User Not Found !!"));
        User lastUser = userRepository.findById(lastUserId).orElseThrow(()-> new ResourceNotFoundException("User Not Found !!"));
        Task task = taskService.getTaskById(idTask);
        Task newTask = taskService.getTaskById(newTaskId);
        if(lastUser.getRmpToken()>0 && !task.isHasChanged() &&( lastUser.equals(userAuth.generateUser()) || task.getCreateBy().equals(userAuth.generateUser()))){
            task.setUser(newUser);
            newTask.setUser(lastUser);
            lastUser.setRmpToken(lastUser.getRmpToken()-1);
            lastUser.getTasks().add(newTask);
            newTask.setAssignedDate(LocalDateTime.now().plusDays(3));
            userRepository.save(lastUser);
            task.setAssignedDate(LocalDateTime.now().plusDays(3));
            task.setHasChanged(true);
            newUser.getTasks().add(task);
            userRepository.save(newUser);
            taskService.createTask(taskMapper.entityToDto(task));
            taskService.createTask(taskMapper.entityToDto(newTask));
            result.put("success", newUser);
            result.put("newTaskAssignedTo", lastUser);
            return result ;
        }
        throw new IllegalAccessException(" Demand Rejected ! ");
    }

    public void DemandChange(Long idUser , Long idTask , TokenType tokenType) throws ResourceNotFoundException {
        User user = userRepository.findById(idUser).orElseThrow(()-> new ResourceNotFoundException("User Not Found !!"));
        Task task = taskService.getTaskById(idTask);
        if(task.isHasChanged()) throw new IllegalArgumentException("This Task already changed");
        if( !tokenType.equals(TokenType.CHANGE_TOKEN) && user.getRmpToken()==0) throw new IllegalArgumentException("Sold Token for Change  insufficient");
        if( !tokenType.equals(TokenType.DELETE_TOKEN) && !user.getSupToken()) throw new IllegalArgumentException("Sold Token for delete  insufficient");
        TaskChangeRequest changeRequest = TaskChangeRequest.builder().dateRequest(LocalDateTime.now())
                .status(StatusRequest.PENDING).task(task)
                .tokenType(tokenType)
                .build();
        taskChangeRequestService.save(changeRequest);
    }
//Supprimer une tache crée par le meme utilisateur n'affecte pas les jetons
    public User DeleteTaskAssigned(Long idUser ,Long idTask) throws Exception {
        User user = userRepository.findById(idUser).orElseThrow(()-> new ResourceNotFoundException("User Not Found !!"));
        Task task = taskService.getTaskById(idTask);
        if(task.getCreateBy().equals(user) || user.getSupToken()){
            for (Task task1: user.getTasks()) {
                if(task1.equals(task)){
                    user.getTasks().remove(task1);
                    task1.setUser(null);
                    taskService.createTask(taskMapper.entityToDto(task1));
                   return userRepository.save(user);
                }
            }
        }
        throw new IllegalArgumentException("");
    }
}
