package com.example.taskflow.service.impl;

import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Entity.Task;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.mapper.TagMapper;
import com.example.taskflow.mapper.TaskMapper;
import com.example.taskflow.mapper.UserMapper;
import com.example.taskflow.repository.TaskRepository;
import com.example.taskflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements com.example.taskflow.service.TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final TagMapper tagMapper;
    private final UserMapper userMapper;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.dtoToEntity(taskDTO);
        task.setCompleted(false);
        Task savedTask = taskRepository.save(task);
        return taskMapper.entityToDto(savedTask);
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws Exception {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = userMapper.dtoToEntity(userService.getUserById(taskDTO.getUserId()));
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
           task.setDescription(taskDTO.getDescription());
           task.setTags(taskDTO.getTags().stream().map(tagMapper::dtoToEntity).collect(Collectors.toList()));
           task.setUser(user);
           task.setCompleted(taskDTO.isCompleted());
          // task.setDueDate(taskDTO.getDueDate());
            Task updatedTask = taskRepository.save(task);
            return taskMapper.entityToDto(updatedTask);
        }
        throw  new Exception("Error Update Failed !!");
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::entityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public Task getTaskById(Long taskId) throws ResourceNotFoundException {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isPresent())
            return optionalTask.get();
        throw new ResourceNotFoundException("Not found This Task ");
    }

    public TaskDTO MakeTaskAsCompleted(Long id_Task) throws ResourceNotFoundException {
        Task task = getTaskById(id_Task);
        if(LocalDateTime.now().isAfter(task.getExpDate()))
            throw  new IllegalArgumentException("Error you can not change Status for this Task");
        task.setCompleted(true);
     return taskMapper.entityToDto(taskRepository.save(task));
    }
}

