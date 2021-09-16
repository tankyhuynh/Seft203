package com.kms.seft203.domain.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String>{
    
    List<Task> findByTaskContaining(String task);
    Task findOneByTask(String task);
    
}
