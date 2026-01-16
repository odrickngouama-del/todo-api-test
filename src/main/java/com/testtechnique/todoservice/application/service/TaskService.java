package com.testtechnique.todoservice.application.service;

import com.testtechnique.todoservice.application.common.Page;
import com.testtechnique.todoservice.application.port.in.TaskUseCases;
import com.testtechnique.todoservice.application.port.out.TaskRepository;
import com.testtechnique.todoservice.domain.exception.NotFoundException;
import com.testtechnique.todoservice.domain.model.Task;

import java.util.List;

import static com.testtechnique.todoservice.domain.policy.TaskPolicies.*;

public class TaskService implements TaskUseCases {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Task> getAll(int page, int size) {
        return paginate(repository.findAll(), page, size);
    }
    @Override
    public Page<Task> getTodo(int page, int size) {
        var todos = repository.findAll()
                .stream().filter(t -> !t.completed()).toList();
        return paginate(todos, page, size);
    }

    @Override
    public Task getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));
    }

    @Override
    public Task add(String label, String description) {
        checkLabelMinLength(label);
        var all = repository.findAll();
        checkMaxActiveTasksNotExceeded(all);
        checkUniqueActiveLabel(all, label);
        return repository.save(Task.newTask(label.trim(), description));
    }

    @Override
    public Task changeStatus(String id, boolean completed) {
        var existing = getById(id);

        if (existing.completed() && !completed) {
            var all = repository.findAll();
            checkMaxActiveTasksNotExceeded(all);
            checkUniqueActiveLabel(all, existing.label());
        }

        return repository.save(existing.withCompleted(completed));
    }

    private Page<Task> paginate(List<Task> all, int page, int size) {

        int from = page * size;
        int to = Math.min(from + size, all.size());

        if (from > all.size()) {
            return new Page<>(List.of(), page, size, all.size(), calcPages(all.size(), size));
        }

        var content = all.subList(from, to);
        return new Page<>(content, page, size, all.size(), calcPages(all.size(), size));
    }

    private int calcPages(long total, int size) {
        return (int) Math.ceil((double) total / size);
    }

}
