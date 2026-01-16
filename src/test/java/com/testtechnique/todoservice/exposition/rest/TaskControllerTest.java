package com.testtechnique.todoservice.exposition.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtechnique.todoservice.application.common.Page;
import com.testtechnique.todoservice.application.port.in.TaskUseCases;
import com.testtechnique.todoservice.commons.dto.TaskRequest;
import com.testtechnique.todoservice.commons.dto.UpdateStatus;
import com.testtechnique.todoservice.domain.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskUseCases useCases;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_get_all_tasks_when_todoOnly_false() throws Exception {

        var page = new Page<>(
                List.of(
                        Task.newTask("Task 1", "d"),
                        Task.newTask("Task 2", "d")
                ),
                0, 10, 2, 1
        );

        when(useCases.getAll(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }


    @Test
    void should_get_only_todo_tasks_when_todoOnly_true() throws Exception {

        var page = new Page<>(
                List.of(Task.newTask("Task 1", "d")),
                0, 10, 1, 1
        );

        when(useCases.getTodo(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/tasks")
                        .param("todoOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }




    @Test
    void should_get_task_by_id() throws Exception {

        var task = Task.newTask("Task", "desc");

        when(useCases.getById("1")).thenReturn(task);

        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Task"));
    }



    @Test
    void should_create_task() throws Exception {

        var task = Task.newTask("Valid label odrick", "desc");

        when(useCases.add("Valid label odrick", "desc")).thenReturn(task);

        var req = new TaskRequest("Valid label odrick", "desc");

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.label").value("Valid label odrick"));
    }

    @Test
    void should_reject_invalid_create_request() throws Exception {

        var req = new TaskRequest("abc", "");

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_update_status() throws Exception {

        var task = Task.newTask("Task", "d").withCompleted(true);

        when(useCases.changeStatus("1", true)).thenReturn(task);

        var req = new UpdateStatus(true);

        mockMvc.perform(patch("/api/v1/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }
}
