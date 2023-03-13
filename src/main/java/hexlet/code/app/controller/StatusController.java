package hexlet.code.app.controller;


import hexlet.code.app.dto.StatusDto;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.service.StatusService;
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

import static hexlet.code.app.controller.StatusController.STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class StatusController {

	public static final String STATUS_CONTROLLER_PATH = "/statuses";
	public static final String ID = "/{id}";

	private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;
	private final StatusRepository statusRepository;
	private final StatusService statusService;

	@Operation(summary = "Create new status")
	@ApiResponse(responseCode = "201", description = "Status created")
	@PostMapping
	@ResponseStatus(CREATED)
	public Status createNewStatus(@RequestBody @Valid final StatusDto statusDto) {
		return statusService.createNewStatus(statusDto);
	}


	@GetMapping
	public List<Status> getAll() {
		List<Status> allStatuses = new ArrayList<>();
		statusRepository.findAll().forEach(allStatuses::add);
		return allStatuses;
	}


	@GetMapping(ID)
	public Status getStatusById(@PathVariable final Long id) {
		return statusRepository.findById(id).get();
	}

	@PutMapping(ID)
	@PreAuthorize(ONLY_OWNER_BY_ID)
	public Status update(@PathVariable final long id, @RequestBody @Valid final StatusDto statusDto) {
		return statusService.updateStatus(id, statusDto);
	}

	@DeleteMapping(ID)
	@PreAuthorize(ONLY_OWNER_BY_ID)
	public void delete(@PathVariable final long id) {
		statusRepository.deleteById(id);
	}


}
