package com.testtechnique.todoservice.infrastructure.repository;

import com.testtechnique.todoservice.application.port.out.TaskRepository;
import com.testtechnique.todoservice.domain.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FakeTaskRepository implements TaskRepository {

    private final Map<String, Task> data = new ConcurrentHashMap<>();

    @Override
    public List<Task> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }
    @Override
    public Task save(Task task) {
        data.put(task.id(), task);
        return task;
    }



}
