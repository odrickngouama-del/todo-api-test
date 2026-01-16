package com.testtechnique.todoservice.commons.dto;


import jakarta.validation.constraints.NotNull;

public record UpdateStatus(
        @NotNull(message = "Completed status is required")
        boolean completed
) {}
