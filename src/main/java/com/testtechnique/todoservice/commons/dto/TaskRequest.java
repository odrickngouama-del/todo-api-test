package com.testtechnique.todoservice.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "Label is required")
        @Size(min = 5, message = "Label must contain at least 5 characters")
        String label,

        @NotBlank(message = "Description is required")
        String description
) {}
