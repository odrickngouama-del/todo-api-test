package com.testtechnique.todoservice.bdd.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TaskSteps {

    @Autowired
    TestRestTemplate http;

    ResponseEntity<Map> last;
    String id;

    @When("I create a task with label {string} and description {string}")
    public void create(String l, String d) {
        last = http.postForEntity("/api/tasks", Map.of("label", l, "description", d), Map.class);
    }

    @Then("status should be {int}")
    public void status(int s) {
        assertThat(last.getStatusCode().value()).isEqualTo(s);
    }

    @Then("I store task id")
    public void store() {
        id = last.getBody().get("id").toString();
    }

    @When("I fetch task by id")
    public void fetch() {
        last = http.getForEntity("/api/tasks/" + id, Map.class);
    }

    @Then("label should be {string}")
    public void label(String l) {
        assertThat(last.getBody().get("label")).isEqualTo(l);
    }
}
