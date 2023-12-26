package com.example.taskflow.service.impl;

import com.example.taskflow.Entity.TaskChangeRequest;
import com.example.taskflow.repository.TaskChangeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskChangeRequestServiceImpl implements com.example.taskflow.service.TaskChangeRequestService {
    private final TaskChangeRequestRepository taskChangeRequestRepository;


    @Override
    public TaskChangeRequest save(TaskChangeRequest taskChangeRequest){
        return taskChangeRequestRepository.save(taskChangeRequest);
    }
}
