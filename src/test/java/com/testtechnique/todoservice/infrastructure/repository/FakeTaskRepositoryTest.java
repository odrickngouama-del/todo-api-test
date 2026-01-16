package com.testtechnique.todoservice.infrastructure.repository;

import com.testtechnique.todoservice.domain.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FakeTaskRepositoryTest {

    private FakeTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeTaskRepository();
    }

    // ---------- SAVE ----------

    @Test
    void should_save_and_find_task_by_id() {

        var task = Task.newTask("Task 1", "desc");

        repository.save(task);

        var found = repository.findById(task.id());

        assertThat(found).isPresent();
        assertThat(found.get().label()).isEqualTo("Task 1");
    }

    // ---------- FIND ALL ----------

    @Test
    void should_return_all_saved_tasks() {

        repository.save(Task.newTask("Task 1", "d"));
        repository.save(Task.newTask("Task 2", "d"));

        List<Task> tasks = repository.findAll();

        assertThat(tasks).hasSize(2);
    }

    @Test
    void should_return_empty_list_when_no_tasks() {

        var tasks = repository.findAll();

        assertThat(tasks).isEmpty();
    }

    // ---------- FIND BY ID ----------

    @Test
    void should_return_empty_when_task_not_found() {

        var result = repository.findById("unknown-id");

        assertThat(result).isEmpty();
    }

    // ---------- UPDATE (save overwrite) ----------

    @Test
    void should_update_existing_task() {

        var task = Task.newTask("Task", "d");
        repository.save(task);

        var updated = task.withCompleted(true);
        repository.save(updated);

        var found = repository.findById(task.id());

        assertThat(found).isPresent();
        assertThat(found.get().completed()).isTrue();
    }
}
