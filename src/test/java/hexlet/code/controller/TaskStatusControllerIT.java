package hexlet.code.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.utils.TestUtils.TEST_EMAIL;
import static hexlet.code.utils.TestUtils.TEST_TASKSTATUS_NAME2;
import static hexlet.code.utils.TestUtils.BASIC_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskStatusControllerIT {


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
    public void createTaskStatus() throws Exception {
        TaskStatusDto newStatus = new TaskStatusDto("testNewStatus");
        final var request = MockMvcRequestBuilders.post(BASIC_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
                                                            .content(asJson(newStatus)).contentType(APPLICATION_JSON);
        utils.perform(request, TEST_EMAIL).andExpect(status().isCreated());

        final TaskStatus expectedStatus = taskStatusRepository.findAll().iterator().next();
        String currentUrl = BASIC_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + TaskStatusController.ID;
        final var response = utils.perform(
                        MockMvcRequestBuilders.get(currentUrl, expectedStatus.getId()), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus status = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedStatus.getName(), status.getName());
    }


    @Test
    public void updateTaskStatus() throws Exception {
        utils.createDefaultTaskStatus();

        final Long taskStatusId = taskStatusRepository.findAll().iterator().next().getId();
        final var status = new TaskStatusDto(TEST_TASKSTATUS_NAME2);

        String currentUrl = BASIC_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + TaskStatusController.ID;
        final var updateRequest = put(currentUrl, taskStatusId)
                .content(asJson(status))
                .contentType(APPLICATION_JSON);
        utils.perform(updateRequest, TEST_EMAIL).andExpect(status().isOk());

        assertEquals(taskStatusRepository.findAll().iterator().next().getName(), TEST_TASKSTATUS_NAME2);
    }


    @Test
    public void deleteTaskStatus() throws Exception {
        utils.createDefaultTaskStatus();
        final Long taskStatusId = taskStatusRepository.findAll().iterator().next().getId();
        String currentUrl = BASIC_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + TaskStatusController.ID;
        utils.perform(delete(currentUrl, taskStatusId), TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, taskStatusRepository.count());

    }
}
