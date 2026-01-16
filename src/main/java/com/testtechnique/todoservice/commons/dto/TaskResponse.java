package com.testtechnique.todoservice.commons.dto;

import com.testtechnique.todoservice.domain.model.Task;

public record TaskResponse(String id, String label, String description, boolean completed) {
    public static TaskResponse from(Task t) {
        return new TaskResponse(t.id(), t.label(), t.description(), t.completed());
    }
}
