package com.testtechnique.todoservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskControllerIT {

    @Autowired
    private TestRestTemplate http;

    @BeforeEach
    void configurePatchSupport() {
        var factory = new HttpComponentsClientHttpRequestFactory();
        http.getRestTemplate().setRequestFactory(factory);
    }

    @Test
    void should_create_task() {
        var req = Map.of("label", "Integration Task", "description", "desc");

        var res = http.postForEntity("/api/v1/tasks", req, Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("id")).isNotNull();
        assertThat(res.getBody().get("label")).isEqualTo("Integration Task");
        assertThat(res.getBody().get("completed")).isEqualTo(false);
    }

    @Test
    void should_get_all_tasks() {

        var res = http.getForEntity("/api/v1/tasks", Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().get("content")).isNotNull();
    }

    @Test
    void should_reject_task_with_short_label() {
        var req = Map.of("label", "Task", "description", "desc");

        var res = http.postForEntity("/api/v1/tasks", req, Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void should_get_incomplete_tasks() {

        var res = http.getForEntity("/api/v1/tasks?todoOnly=true", Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().get("content")).isNotNull();
    }

    @Test
    void should_return_only_incomplete_tasks() {

        http.postForEntity("/api/v1/tasks",
                Map.of("label","Task1","description","d"), Map.class);

        var created = http.postForEntity("/api/v1/tasks",
                Map.of("label","Task2","description","d"), Map.class);

        var id = created.getBody().get("id").toString();

        var request = new HttpEntity<>(Map.of("completed", true));

        var patch = http.exchange(
                "/api/v1/tasks/" + id + "/status",
                HttpMethod.PATCH,
                request,
                Map.class
        );

        assertThat(patch.getStatusCode()).isEqualTo(HttpStatus.OK);

        var resp = http.getForEntity("/api/v1/tasks?todoOnly=true", Map.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);

        var content = (List<Map<String, Object>>) resp.getBody().get("content");

        assertThat(content).hasSize(1);
        assertThat(content.get(0).get("completed")).isEqualTo(false);
        assertThat(content.get(0).get("label")).isEqualTo("Task1");
    }


    @Test
    void should_get_task_by_id() {

        var create = http.postForEntity("/api/v1/tasks",
                Map.of("label","TaskID","description","d"), Map.class);

        var id = create.getBody().get("id").toString();

        var res = http.getForEntity("/api/v1/tasks/"+id, Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("label")).isEqualTo("TaskID");
    }

    @Test
    void should_return_paginated_tasks() {

        for (int i = 1; i <= 12; i++) {
            http.postForEntity("/api/v1/tasks",
                    Map.of("label","Task "+i,"description","d"), Map.class);
        }

        var res = http.getForEntity("/api/v1/tasks?page=0&size=5", Map.class);

        assertThat(((List<?>) res.getBody().get("content"))).hasSize(5);
        assertThat(res.getBody().get("totalElements")).isEqualTo(10);
        assertThat(res.getBody().get("totalPages")).isEqualTo(2);
    }


}