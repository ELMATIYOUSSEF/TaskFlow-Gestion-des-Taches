package com.example.taskflow.service;

import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Entity.Task;
import com.example.taskflow.exception.ResourceNotFoundException;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws Exception;

    void deleteTask(Long taskId);

    List<TaskDTO> getAllTasks();

    TaskDTO MakeTaskAsCompleted(Long id_Task) throws ResourceNotFoundException;

    Task getTaskById(Long taskId) throws ResourceNotFoundException;
}
