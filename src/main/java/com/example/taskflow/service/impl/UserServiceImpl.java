package com.example.taskflow.service.impl;


import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Dto.response.UserVM;
import com.example.taskflow.Entity.Enums.StatusRequest;
import com.example.taskflow.Entity.Enums.TokenType;
import com.example.taskflow.Entity.Role;
import com.example.taskflow.Entity.Task;
import com.example.taskflow.Entity.TaskChangeRequest;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.repository.UserRepository;
import com.example.taskflow.security.UserAuth;
import com.example.taskflow.service.TaskChangeRequestService;
import com.example.taskflow.service.TaskService;
import com.example.taskflow.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TaskService taskService;
    private final UserAuth userAuth;
    private final TaskChangeRequestService taskChangeRequestService;


    @Override
    public UserVM createUser(UserDTO userDTO) throws ResourceNotFoundException {
        userDTO.getAuthorities().add( Role.builder().id(2L).name("USER").build());
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if(userOptional.isPresent()) throw new ResourceNotFoundException("This Email Is Already Exist !");
        return modelMapper.map(userRepository.save(modelMapper.map(userDTO,User.class)),UserVM.class);
    }

    public  UserDTO findByEmail(String email) throws ResourceNotFoundException {
       Optional<User> userOptional = userRepository.findByEmail(email);
       if(userOptional.isEmpty()) throw new ResourceNotFoundException("No User with this Email");
       return modelMapper.map(userOptional.get() , UserDTO.class);
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
        User user = modelMapper.map(userDTO ,User.class);
       return modelMapper.map(userRepository.save(user),UserDTO.class) ;
    }

    public String logout(String token) throws IllegalAccessException {
        if (token != null && token.startsWith("Bearer ")) {
            String authToken = token.substring(7);
                return "Logged out successfully";
            }
       throw new IllegalAccessException("Logout is Rejected");
    }

    @Override
    public UserVM updateUser(Long userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            Optional<User> email = userRepository.findByEmail(user.getEmail());
            if(email.isPresent()) throw  new IllegalArgumentException("this Email Already Exist ! ");
            user.setPassword(userDTO.getPassword());
            User updatedUser = userRepository.save(user);
            return modelMapper.map(updatedUser,UserVM.class);
        } else {
            return null;
        }
    }

    @Override
    public String deleteUser(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("no user found for deleting "));
        userRepository.deleteById(user.getId());
        return "Deleting User with successfully";
    }

    @Override
    public List<UserVM> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserVM.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(user -> modelMapper.map(user,UserDTO.class)).orElse(null);
    }

    public TaskDTO SelfAssignTask(Long idUser, Long idTask) throws Exception {
        Task task = taskService.getTaskById(idTask);
        task.setAssignedDate(LocalDateTime.now().plusDays(3));
       if( task.getExpDate().isBefore(LocalDateTime.now())) throw new IllegalArgumentException("this Task is Completed");
       if(task.getExpDate().isBefore(task.getAssignedDate())) throw new IllegalArgumentException(" you can not assigned this Task cause Acceptable Date for getting it is Expired ");
       Optional<User> user = userRepository.findById(idUser);
        if(user.isPresent() && user.get().equals(userAuth.generateUser())){
            task.setUser(user.get());
          return taskService.createTask(modelMapper.map(task,TaskDTO.class));
        }
        throw new IllegalArgumentException("you have not access to assign this Task for authors ");
    }
    @Transactional
    public Map<String, User> changeTask(Long adminUserId ,Long idTask , Long newUserId , Long lastUserId , Long newTaskId) throws Exception {

        ValidateInputForChangeTask(idTask,newUserId,lastUserId,newTaskId ,adminUserId);
        security(adminUserId);
        if (lastUserId.equals(newUserId)) {
            throw new IllegalArgumentException("For changing tasks, the new user must be different from the last user.");
        }
        Map<String, User> result = new HashMap<>();

        User newUser = userRepository.findById(newUserId).orElseThrow(() -> new ResourceNotFoundException("NewUser Not Found !!"));
        User lastUser = userRepository.findById(lastUserId).orElseThrow(() -> new ResourceNotFoundException("LastUser Not Found !!"));
        Task task = taskService.getTaskById(idTask);
        Task newTask = taskService.getTaskById(newTaskId);

        if (lastUser.getRmpToken() > 0 && !task.isHasChanged()
                && (lastUser.equals(userAuth.generateUser()) || task.getCreateBy().equals(userAuth.generateUser()))) {
            task.setUser(newUser);
            newTask.setUser(lastUser);

            lastUser.getTasks().add(newTask);
            newTask.setAssignedDate(LocalDateTime.now().plusDays(3));
            userRepository.save(lastUser);

            task.setAssignedDate(LocalDateTime.now().plusDays(3));
            task.setHasChanged(true);
            newUser.getTasks().add(task);
            userRepository.save(newUser);

            taskService.createTask(modelMapper.map(task, TaskDTO.class));
            taskService.createTask(modelMapper.map(newTask, TaskDTO.class));

            result.put("success", newUser);
            result.put("newTaskAssignedTo", lastUser);
            return result;
        }
        throw new IllegalAccessException("Demand Rejected. Insufficient tokens .");
    }
@Override
    public TaskChangeRequest demandChange(Long idUser, Long idTask, TokenType tokenType) throws ResourceNotFoundException, IllegalAccessException {

        validateInput( idUser , idTask);

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found !!"));

        Task task = taskService.getTaskById(idTask);
        if (task.isHasChanged()) {
            throw new IllegalArgumentException("This Task has already been changed");
        }

        handleTokenOperation(user,tokenType);

        TaskChangeRequest changeRequest = TaskChangeRequest.builder()
                .dateRequest(LocalDateTime.now())
                .status(StatusRequest.PENDING)
                .task(task)
                .tokenType(tokenType)
                .build();

        userRepository.save(user);
        return taskChangeRequestService.save(changeRequest);
    }
@Override
    public User DeleteTaskAssigned(Long idUser ,Long idTask) throws Exception {
        User user = userRepository.findById(idUser).orElseThrow(()-> new ResourceNotFoundException("User Not Found !!"));
        Task task = taskService.getTaskById(idTask);

        if(task.getCreateBy().equals(user) || user.getSupToken()){

            for (Task task1: user.getTasks()) {

                if(task1.equals(task)){
                    user.getTasks().remove(task1);
                    task1.setUser(null);
                    taskService.createTask(modelMapper.map(task1,TaskDTO.class));
                   return userRepository.save(user);
                }
            }
        }
        throw new IllegalArgumentException("failed deleting");
    }

    private void handleTokenOperation(User user, TokenType tokenType) {
        switch (tokenType) {
            case CHANGE_TOKEN:
                handleChangeToken(user);
                break;
            case DELETE_TOKEN:
                handleDeleteToken(user);
                break;
            default:
                throw new IllegalArgumentException("Unsupported TokenType");
        }
    }

    private void handleChangeToken(User user) {
        if (user.getRmpToken() == 0) {
            throw new IllegalArgumentException("Insufficient tokens for change");
        }
        user.setRmpToken(user.getRmpToken() - 1);
    }

    private void handleDeleteToken(User user) {
        if (user.getSupToken()) {
            throw new IllegalArgumentException("Insufficient tokens for delete");
        }
        user.setSupToken(true);
    }

    public void validateInput(Long idUser , Long idTask){
        Objects.requireNonNull(idUser, "idUser must not be null");
        Objects.requireNonNull(idTask, "idTask must not be null");
        if (idUser <= 0 || idTask <= 0) {
            throw new IllegalArgumentException("Invalid idUser or idTask");
        }
    }
    public void ValidateInputForChangeTask(Long idTask , Long newUserId , Long lastUserId , Long newTaskId ,Long adminUserId){
        Objects.requireNonNull(idTask, "idTask must not be null");
        Objects.requireNonNull(adminUserId, "adminUserId must not be null");
        Objects.requireNonNull(newUserId, "newUserId must not be null");
        Objects.requireNonNull(lastUserId, "lastUserId must not be null");
        Objects.requireNonNull(newTaskId, "newTaskId must not be null");
    }
    private void taskCannotAssignedInThePast(Task task) {
        if (task.getAssignedDate() != null && task.getAssignedDate().isBefore(LocalDateTime.now()) ){
            throw new IllegalArgumentException("The date is in the past !");
        }
    }

    public void validateInput(Long adminUserId, Long userId, Long taskId) {
        Objects.requireNonNull(adminUserId, "adminUserId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(taskId, "taskId must not be null");
        if (adminUserId <= 0 || userId <= 0 || taskId <= 0) {
            throw new IllegalArgumentException("Invalid adminUserId, userId, or taskId");
        }
    }
@Override
    public UserVM adminAssignTaskToUser(Long adminUserId, Long userIdToAssign, Long taskIdToAssign) throws Exception {

        validateInput(adminUserId, userIdToAssign, taskIdToAssign);

        security(adminUserId);

        User targetUser = userRepository.findById(userIdToAssign)
                .orElseThrow(() -> new ResourceNotFoundException("Target User Not Found !!"));

        Task taskToAssign = taskService.getTaskById(taskIdToAssign);

        if (taskToAssign.getExpDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("This Task is already completed");
        }

        taskToAssign.setAssignedDate(LocalDateTime.now().plusDays(3));
        taskToAssign.setUser(targetUser);
        targetUser.getTasks().add(taskToAssign);

        userRepository.save(targetUser);
        taskService.createTask(modelMapper.map(taskToAssign, TaskDTO.class));

        return modelMapper.map(targetUser, UserVM.class);
    }

    public void security(Long admin) throws IllegalAccessException, ResourceNotFoundException {
        User adminUser = userRepository.findById(admin)
                .orElseThrow(() -> new ResourceNotFoundException("Admin User Not Found !!"));
        if (!adminUser.equals(userAuth.generateUser())) {
            throw new IllegalAccessException("You do not have permission to perform this action");
        }
    }

}
