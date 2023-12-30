package com.example.taskflow.web.rest;
import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Dto.response.UserVM;
import com.example.taskflow.Entity.Enums.TokenType;
import com.example.taskflow.Entity.TaskChangeRequest;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public UserVM createUser(@Valid @RequestBody UserDTO userDTO) throws ResourceNotFoundException {
        return userService.createUser(userDTO);
    }

    @GetMapping("/{userId}")
    public UserVM getUserById(@PathVariable Long userId) {
        return  modelMapper.map(userService.getUserById(userId), UserVM.class);
    }
    @GetMapping("/all")
    public List<UserVM> getAllUsers() {
        return userService.getAllUsers();
    }
    @PutMapping("/{userId}")
    public UserVM updateUser( @Valid @PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO);
    }
    @PostMapping("/selfAssignTask")
    public TaskDTO selfAssignTask(@RequestParam Long idUser, @RequestParam Long idTask) throws Exception {
        return userService.SelfAssignTask(idUser, idTask);
    }
    @Transactional
    @PostMapping("/changeTask")
    public Map<String, User> changeTask(@RequestParam Long idAdmin ,@RequestParam Long idTask, @RequestParam Long newUserId,
                                        @RequestParam Long lastUserId, @RequestParam Long newTaskId) throws Exception {
        return userService.changeTask(idAdmin ,idTask, newUserId, lastUserId, newTaskId);
    }
    @PostMapping( "/demandChange")
    public ResponseEntity<TaskChangeRequest>  demandChange(@RequestParam Long idUser, @RequestParam Long idTask, @RequestParam String tokenType) throws ResourceNotFoundException, IllegalAccessException {
        TaskChangeRequest taskChangeRequest = userService.demandChange(idUser, idTask, TokenType.valueOf(tokenType));
        return ResponseEntity.ok().body(taskChangeRequest);
    }
    @PostMapping("/deleteTaskAssigned")
    public User deleteTaskAssigned(@RequestParam Long idUser, @RequestParam Long idTask) throws Exception {
        return userService.DeleteTaskAssigned(idUser, idTask);
    }

    @PostMapping("/assignTask")
    public UserVM adminAssignTaskToUser(@RequestParam Long adminUserId,@RequestParam Long userIdToAssign,@RequestParam Long taskIdToAssign) throws Exception {
        return userService.adminAssignTaskToUser(adminUserId,userIdToAssign,taskIdToAssign);
    }
}
