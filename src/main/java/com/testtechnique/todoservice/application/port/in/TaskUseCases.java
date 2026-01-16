package com.testtechnique.todoservice.application.port.in;

import com.testtechnique.todoservice.application.common.Page;
import com.testtechnique.todoservice.domain.model.Task;

import java.util.List;

public interface TaskUseCases {
    Page<Task> getAll(int page, int size);
    Page<Task> getTodo(int page, int size);
    Task getById(String id);
    Task add(String label, String description);
    Task changeStatus(String id, boolean completed);

}
