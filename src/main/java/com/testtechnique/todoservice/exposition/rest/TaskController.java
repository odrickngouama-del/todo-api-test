package com.testtechnique.todoservice.exposition.rest;


import com.testtechnique.todoservice.application.port.in.TaskUseCases;
import com.testtechnique.todoservice.commons.dto.PageResponse;
import com.testtechnique.todoservice.commons.dto.TaskRequest;
import com.testtechnique.todoservice.commons.dto.TaskResponse;
import com.testtechnique.todoservice.commons.dto.UpdateStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskUseCases useCases;

    public TaskController(TaskUseCases useCases) {
        this.useCases = useCases;
    }


    @GetMapping
    public PageResponse<TaskResponse> getAll(
            @RequestParam(defaultValue = "false") boolean todoOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        var result = todoOnly
                ? useCases.getTodo(page, size)
                : useCases.getAll(page, size);

        return new PageResponse<>(
                result.content().stream().map(TaskResponse::from).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }



    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable String id) {
        return TaskResponse.from(useCases.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest req) {
        return TaskResponse.from(useCases.add(req.label(), req.description()));
    }



    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(@PathVariable String id,
                                     @Valid @RequestBody UpdateStatus req) {
        return TaskResponse.from(useCases.changeStatus(id, req.completed()));
    }
}
