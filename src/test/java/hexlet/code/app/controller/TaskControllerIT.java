package hexlet.code.app.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.TaskController.ID;
import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskControllerIT {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TestUtils utils;

	@BeforeEach
	public void before() throws Exception {
		utils.regDefaultUser();
	}

	@AfterEach
	public void clear() {
		utils.tearDown();
	}


	@Test
	public void createTask() throws Exception {
		utils.createDefaultStatus();
		
		TaskDto newTask = new TaskDto("firstTask", "importantDescription", 1L, 1L);
		final var request = post("/api" + TASK_CONTROLLER_PATH)
															.content(asJson(newTask)).contentType(APPLICATION_JSON);
		utils.perform(request, TEST_EMAIL).andExpect(status().isCreated());

		final Task expectedTask = taskRepository.findAll().iterator().next();
		final var response = utils.perform(
						get("/api/tasks/1",
								expectedTask.getId()), TEST_EMAIL)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse();

		final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
		});

		assertEquals(expectedTask.getName(), task.getName());
	}

}
