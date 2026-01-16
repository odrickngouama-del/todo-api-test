package com.testtechnique.todoservice.domain.policy;

import com.testtechnique.todoservice.domain.exception.DomainException;
import com.testtechnique.todoservice.domain.model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskPoliciesTest {

    @Test
    void should_accept_valid_label() {
        TaskPolicies.checkLabelMinLength("Valid label");
    }

    @Test
    void should_reject_null_label() {
        assertThatThrownBy(() ->
                TaskPolicies.checkLabelMinLength(null)
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void should_reject_blank_label() {
        assertThatThrownBy(() ->
                TaskPolicies.checkLabelMinLength("   ")
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void should_reject_short_label() {
        assertThatThrownBy(() ->
                TaskPolicies.checkLabelMinLength("abc")
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void should_accept_when_less_than_10_active_tasks() {

        var tasks = IntStream.range(0, 9)
                .mapToObj(i -> Task.newTask("Task" + i, "d"))
                .toList();

        TaskPolicies.checkMaxActiveTasksNotExceeded(tasks);
    }

    @Test
    void should_reject_when_10_active_tasks() {

        var tasks = IntStream.range(0, 10)
                .mapToObj(i -> Task.newTask("Task" + i, "d"))
                .toList();

        assertThatThrownBy(() ->
                TaskPolicies.checkMaxActiveTasksNotExceeded(tasks)
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void should_ignore_completed_tasks_in_limit() {

        var completed = Task.newTask("Done", "x").withCompleted(true);

        var tasks = IntStream.range(0, 10)
                .mapToObj(i -> Task.newTask("Task" + i, "d"))
                .toList();

        var list = new java.util.ArrayList<>(tasks);
        list.add(completed); // 11 tasks total, but only 10 active

        assertThatThrownBy(() ->
                TaskPolicies.checkMaxActiveTasksNotExceeded(list)
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void should_accept_unique_label() {

        var tasks = List.of(
                Task.newTask("Courses", "x"),
                Task.newTask("Sport", "x")
        );

        TaskPolicies.checkUniqueActiveLabel(tasks, "Cinema");
    }

    @Test
    void should_reject_duplicate_label_case_insensitive() {

        var tasks = List.of(
                Task.newTask("Courses", "x")
        );

        assertThatThrownBy(() ->
                TaskPolicies.checkUniqueActiveLabel(tasks, "courses")
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void should_ignore_completed_tasks_for_duplicate_check() {

        var completed = Task.newTask("Courses", "x").withCompleted(true);

        var tasks = List.of(completed);

        TaskPolicies.checkUniqueActiveLabel(tasks, "courses");
    }
}
