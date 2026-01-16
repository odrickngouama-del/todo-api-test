package com.testtechnique.todoservice.application.port.out;

import com.testtechnique.todoservice.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Optional<Task> findById(String id);
    Task save(Task task);
}
