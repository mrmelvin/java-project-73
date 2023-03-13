package hexlet.code.app.controller;


import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

	public static final String TASK_CONTROLLER_PATH = "/tasks";

	public static final String ID = "/{id}";

	private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

	private TaskRepository taskRepository;

	private TaskService taskService;

	@Operation(summary = "Create new status")
	@ApiResponse(responseCode = "201", description = "Task created")
	@PostMapping
	@ResponseStatus(CREATED)
	public Task createNewTask(@RequestBody @Valid final TaskDto taskDto) {
		return taskService.createNewTask(taskDto);
	}

	@GetMapping
	public List<Task> getAll() {
		List<Task> allTasks = new ArrayList<>();
		taskRepository.findAll().forEach(allTasks::add);
		return allTasks;
	}


	@GetMapping(ID)
	public Task getTaskById(@PathVariable final Long id) {
		return taskRepository.findById(id).get();
	}

	@PutMapping(ID)
	@PreAuthorize(ONLY_OWNER_BY_ID)
	public Task update(@PathVariable final long id, @RequestBody @Valid final TaskDto taskDto) {
		return taskService.updateTask(id, taskDto);
	}

	@DeleteMapping(ID)
	@PreAuthorize(ONLY_OWNER_BY_ID)
	public void delete(@PathVariable final long id) {
		taskRepository.deleteById(id);
	}
}
