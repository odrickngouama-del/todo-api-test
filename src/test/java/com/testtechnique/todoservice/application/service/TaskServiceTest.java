package com.testtechnique.todoservice.application.service;


import com.testtechnique.todoservice.application.port.out.TaskRepository;
import com.testtechnique.todoservice.domain.exception.DomainException;
import com.testtechnique.todoservice.domain.exception.NotFoundException;
import com.testtechnique.todoservice.domain.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository repository;

    @InjectMocks
    TaskService service;


    @Test
    void should_paginate_all_tasks() {
        var tasks = List.of(
                Task.newTask("A", "d"),
                Task.newTask("B", "d"),
                Task.newTask("C", "d")
        );

        when(repository.findAll()).thenReturn(tasks);

        var page = service.getAll(0, 2);

        assertThat(page.content()).hasSize(2);
        assertThat(page.totalElements()).isEqualTo(3);
        assertThat(page.totalPages()).isEqualTo(2);
    }

    @Test
    void should_return_empty_page_if_page_out_of_bounds() {
        when(repository.findAll()).thenReturn(List.of(Task.newTask("A", "d")));

        var page = service.getAll(5, 2);

        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isEqualTo(1);
    }


    @Test
    void should_return_only_incomplete_tasks() {
        var t1 = Task.newTask("A", "d");
        var t2 = Task.newTask("B", "d").withCompleted(true);

        when(repository.findAll()).thenReturn(List.of(t1, t2));

        var page = service.getTodo(0, 10);

        assertThat(page.content()).containsExactly(t1);
    }


    @Test
    void should_return_task_by_id() {
        var task = Task.newTask("A", "d");

        when(repository.findById(task.id())).thenReturn(Optional.of(task));

        var result = service.getById(task.id());

        assertThat(result).isEqualTo(task);
    }

    @Test
    void should_throw_not_found_when_id_unknown() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById("x"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void should_add_task_when_rules_are_ok() {
        when(repository.findAll()).thenReturn(List.of());
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        var task = service.add("Valid label", "desc");

        assertThat(task.label()).isEqualTo("Valid label");
        verify(repository).save(any(Task.class));
    }

    @Test
    void should_fail_when_label_too_short() {
        assertThatThrownBy(() -> service.add("abc", "d"))
                .isInstanceOf(DomainException.class);
    }


    @Test
    void should_change_status_to_completed() {
        var task = Task.newTask("A", "d");

        when(repository.findById(task.id())).thenReturn(Optional.of(task));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        var updated = service.changeStatus(task.id(), true);

        assertThat(updated.completed()).isTrue();
    }

    @Test
    void should_revalidate_when_reactivating_task() {
        var completed = Task.newTask("A", "d").withCompleted(true);

        when(repository.findById(completed.id())).thenReturn(Optional.of(completed));
        when(repository.findAll()).thenReturn(List.of(completed));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        var updated = service.changeStatus(completed.id(), false);

        assertThat(updated.completed()).isFalse();
    }
}
