package com.testtechnique.todoservice.application.common;

import java.util.List;

public record Page<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
