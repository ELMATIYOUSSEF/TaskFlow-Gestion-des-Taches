package com.example.taskflow.repository;

import com.example.taskflow.Entity.TaskChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskChangeRequestRepository extends JpaRepository<TaskChangeRequest ,Long> {
}
