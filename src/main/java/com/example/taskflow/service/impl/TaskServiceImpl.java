package com.example.taskflow.service.impl;

import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Entity.Enums.StatusTask;
import com.example.taskflow.Entity.Tag;
import com.example.taskflow.Entity.Task;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.mapper.TagMapper;
import com.example.taskflow.mapper.TaskMapper;
import com.example.taskflow.mapper.UserMapper;
import com.example.taskflow.repository.TagRepository;
import com.example.taskflow.repository.TaskRepository;
import com.example.taskflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements com.example.taskflow.service.TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final TagMapper tagMapper;
    private final UserMapper userMapper;
    private final TagRepository tagRepository;



    @Override
    public TaskDTO createTask(TaskDTO taskDTO) throws Exception {
        Task task = taskMapper.dtoToEntity(taskDTO);
        taskCannotCreateInThePast(task);
        List<Tag> tags = validateTags(task);
        task.setTags(tags);
        task.setUserAssignedBefore(task.getUser().getId());
        restrictTaskScheduling(task);
        task.setStatusTask(StatusTask.NO_COMPLETED);
        task.setHasChanged(false);
        return taskMapper.entityToDto(taskRepository.save(task));
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
    @Override
    public TaskDTO MakeTaskAsCompleted(Long id_Task) throws ResourceNotFoundException {
        Task task = getTaskById(id_Task);
        if(LocalDateTime.now().isAfter(task.getExpDate()))
            throw  new IllegalArgumentException("Error you can not change Status for this Task");
        task.setStatusTask(StatusTask.COMPLETED);
     return taskMapper.entityToDto(taskRepository.save(task));
    }

    private void taskCannotCreateInThePast(Task task) {
        if (task.getAssignedDate() != null && task.getAssignedDate().isBefore(LocalDateTime.now()) ){
            throw new IllegalArgumentException("The date is in the past !");
        }
    }
    private List<Tag> validateTags (Task task) {
        if (task.getTags() == null || task.getTags().size() < 2) {
            throw new IllegalArgumentException("At least 2 tags are required!");
        }
        Set<Tag> existingTags = new HashSet<>(tagRepository.findAll());
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : task.getTags()) {
            if (!existingTags.contains(tag)) {
                tagRepository.save(tag);
                tags.add(tag);
            }
        }
        return tags;
    }


    private void restrictTaskScheduling(Task task) throws Exception {
        LocalDateTime maxAllowedExpDate = LocalDateTime.now().plusDays(3);
        if (task.getExpDate().isBefore(maxAllowedExpDate)) {
            throw new Exception("Expiration date cannot be before 3 days from now!");
        }
        if (task.getAssignedDate().isBefore(LocalDateTime.now())) {
            throw new Exception("The Task cannot be assigned before today!");
        }
    }
}

