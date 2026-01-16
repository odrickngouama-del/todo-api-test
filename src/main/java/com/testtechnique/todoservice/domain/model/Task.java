package com.testtechnique.todoservice.domain.model;

import java.util.UUID;

public class Task {

    private final String id;
    private final String label;
    private final String description;
    private final boolean completed;

    private Task(String id, String label, String description, boolean completed) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.completed = completed;
    }

    public Task update(String label, String description, boolean completed) {
        return new Task(this.id, label, description, completed);
    }
    public static Task newTask(String label, String description) {
        return new Task(UUID.randomUUID().toString(), label, description, false);
    }

    public Task withCompleted(boolean completed) {
        return new Task(id, label, description, completed);
    }

    public String id() { return id; }
    public String label() { return label; }
    public String description() { return description; }
    public boolean completed() { return completed; }
}
