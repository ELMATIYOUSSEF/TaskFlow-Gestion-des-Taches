package com.example.taskflow.service;

import com.example.taskflow.Entity.TaskChangeRequest;
import org.springframework.stereotype.Service;

@Service
public interface TaskChangeRequestService {
    TaskChangeRequest save(TaskChangeRequest taskChangeRequest);
}
