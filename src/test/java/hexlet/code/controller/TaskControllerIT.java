package hexlet.code.controller;

import hexlet.code.config.SpringConfigForIT;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Set;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskControllerIT {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

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
        utils.createDefaultTask();
        final Task expectedTask = taskRepository.findAll().get(0);
        String currentUrl = "/api" + TASK_CONTROLLER_PATH + ID;
        final var response = utils.perform(get(currentUrl, expectedTask.getId()), TestUtils.TEST_EMAIL)
                                                        .andExpect(status().isOk())
                                                        .andReturn()
                                                        .getResponse();

        final Task task = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTask.getName(), task.getName());
    }


    @Test
    public void updateTaskStatus() throws Exception {
        utils.createDefaultTask();

        final Long taskId = taskRepository.findAll().get(0).getId();
        final User user = userRepository.findAll().get(0);
        final TaskStatus taskStatus = taskStatusRepository.findAll().get(0);
        final Label label = labelRepository.findAll().get(0);
        final var newTask = new TaskDto(TestUtils.TEST_TASK_NAME2,
                TestUtils.TEST_TASK_DESCRIPTION2,
                user.getId(),
                taskStatus.getId(),
                Set.of(label.getId()));

        String currentUrl = "/api" + TASK_CONTROLLER_PATH + ID;
        final var updateRequest = put(currentUrl, taskId)
                .content(TestUtils.asJson(newTask))
                .contentType(APPLICATION_JSON);
        utils.perform(updateRequest, TestUtils.TEST_EMAIL).andExpect(status().isOk());

        final Task expectedTask = taskRepository.findAll().get(0);
        final var response = utils.perform(get(currentUrl, expectedTask.getId()), TestUtils.TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Task task = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTask.getName(), task.getName());
    }


    @Test
    public void deleteTaskStatus() throws Exception {
        utils.createDefaultTask();
        final Long taskId = taskRepository.findAll().get(0).getId();
        String currentUrl = "/api" + TASK_CONTROLLER_PATH + TaskController.ID;
        utils.perform(delete(currentUrl, taskId), TestUtils.TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, taskRepository.count());

    }

}
