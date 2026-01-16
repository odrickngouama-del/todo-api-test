package com.testtechnique.todoservice.configuration;

import com.testtechnique.todoservice.application.port.out.TaskRepository;
import com.testtechnique.todoservice.application.service.TaskService;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    TaskService taskService(TaskRepository repo) {
        return new TaskService(repo);
    }
}
