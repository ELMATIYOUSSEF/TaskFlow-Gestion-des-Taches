package com.example.taskflow.service.impl;

import com.example.taskflow.Dto.TagDTO;
import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Entity.Enums.StatusTask;
import com.example.taskflow.Entity.Tag;
import com.example.taskflow.Entity.Task;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.repository.TagRepository;
import com.example.taskflow.repository.TaskRepository;
import com.example.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements com.example.taskflow.service.TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;



    @Override
    public TaskDTO createTask(TaskDTO taskDTO) throws Exception {
        Task task = modelMapper.map(taskDTO , Task.class);
        List<Tag> tags = validateTags(task);
        task.setDateCreate(LocalDateTime.now());
        task.setTags(tags);
        restrictTaskScheduling(task);
        task.setStatusTask(StatusTask.NO_COMPLETED);
        task.setHasChanged(false);
        return modelMapper.map(taskRepository.save(task) , TaskDTO.class);
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws Exception {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(task.getTitle());
            validateTags (task);
            task.setDescription(taskDTO.getDescription());
            task.setTags(taskDTO.getTags().stream().map(tag ->modelMapper.map(tag,Tag.class)).collect(Collectors.toList()));
            Task updatedTask = taskRepository.save(task);
            return modelMapper.map(updatedTask , TaskDTO.class);
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
                .map(task -> modelMapper.map(task , TaskDTO.class))
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
     return modelMapper.map(taskRepository.save(task) ,TaskDTO.class);
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
        LocalDateTime maxAllowedExpDate = LocalDateTime.now().plusDays(2);
        if (task.getExpDate().isBefore(maxAllowedExpDate) ) {
            throw new Exception("Expiration date cannot be before 2 days from now!");
        }
        if (task.getAssignedDate().isBefore(LocalDateTime.now())) {
            throw new Exception("The Task cannot be assigned in the Past !");
        }
    }
}

