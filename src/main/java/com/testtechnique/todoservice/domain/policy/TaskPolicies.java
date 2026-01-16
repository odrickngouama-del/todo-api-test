package com.testtechnique.todoservice.domain.policy;

import com.testtechnique.todoservice.domain.exception.DomainException;
import com.testtechnique.todoservice.domain.model.Task;

import java.util.List;

public class TaskPolicies {
    private static final int MIN_LABEL_LENGTH = 5;
    private static final int MAX_INCOMPLETE_TASKS = 10;


    private TaskPolicies() {}

    public static void checkLabelMinLength(String label) {
        if (label == null || label.trim().length() < MIN_LABEL_LENGTH) {
            throw new DomainException("LABEL_TOO_SHORT", "Label must contain at least 5 characters");
        }
    }

    public static void checkMaxActiveTasksNotExceeded(List<Task> tasks) {
        long active = tasks.stream().filter(t -> !t.completed()).count();
        if (active >= MAX_INCOMPLETE_TASKS) {
            throw new DomainException("MAX_ACTIVE_TASKS", "Maximum of 10 active tasks allowed");
        }
    }

    public static void checkUniqueActiveLabel(List<Task> tasks, String label) {
        boolean exists = tasks.stream()
                .anyMatch(t -> !t.completed() && t.label().equalsIgnoreCase(label.trim()));
        if (exists) {
            throw new DomainException("DUPLICATE_ACTIVE_LABEL", "Active task with same label already exists");
        }
    }
}
