package hexlet.code.app.controller;


import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusService taskStatusService;

    @Operation(summary = "Create new status")
    @ApiResponse(responseCode = "201", description = "Status created")
    @PostMapping
    @ResponseStatus(CREATED)
    public TaskStatus createNewTaskStatus(@RequestBody @Valid final TaskStatusDto statusDto) {
        return taskStatusService.createNewTaskStatus(statusDto);
    }


    @ApiResponses(@ApiResponse(responseCode = "200", content =
        @Content(schema = @Schema(implementation = TaskStatus.class))
    ))
    @GetMapping
    public List<TaskStatus> getAll() {
        List<TaskStatus> allTaskStatuses = new ArrayList<>();
        taskStatusRepository.findAll().forEach(allTaskStatuses::add);
        return allTaskStatuses;
    }


    @ApiResponses(@ApiResponse(responseCode = "200"))
    @GetMapping(ID)
    public TaskStatus getTaskStatusById(@PathVariable final Long id) {
        return taskStatusRepository.findById(id).get();
    }

    @ApiResponses(@ApiResponse(responseCode = "200"))
    @PutMapping(ID)
    public TaskStatus update(@PathVariable final long id, @RequestBody @Valid final TaskStatusDto statusDto) {
        return taskStatusService.updateTaskStatus(id, statusDto);
    }

    @ApiResponses(@ApiResponse(responseCode = "200"))
    @DeleteMapping(ID)
    public void delete(@PathVariable final long id) {
        taskStatusRepository.deleteById(id);
    }


}
