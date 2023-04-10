package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {

	public static final String LABEL_CONTROLLER_PATH = "/labels";
	public static final String ID = "/{id}";
	
	private final LabelRepository labelRepository;
	
	private final LabelService labelService;

	@Operation(summary = "Create new label")
	@ApiResponse(responseCode = "201", description = "Label created")
	@PostMapping
	@ResponseStatus(CREATED)
	public Label createNewLabel(@RequestBody @Valid final LabelDto labelDto) {
		return labelService.createLabel(labelDto);
	}

	@GetMapping
	public List<Label> getAll() {
		List<Label> allLabels = new ArrayList<>();
		labelRepository.findAll().forEach(allLabels::add);
		return allLabels;
	}


	@GetMapping(ID)
	public Label getLabelById(@PathVariable final Long id) {
		return labelRepository.findById(id).get();
	}

	@PutMapping(ID)
	public Label update(@PathVariable final long id, @RequestBody @Valid final LabelDto statusDto) {
		return labelService.updateLabel(id, statusDto);
	}

	@DeleteMapping(ID)
	public void delete(@PathVariable final long id) {
		labelRepository.deleteById(id);
	}
}
