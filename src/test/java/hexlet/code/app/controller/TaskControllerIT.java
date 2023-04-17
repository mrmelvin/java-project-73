package hexlet.code.app.controller;

import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
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
//import ch.qos.logback.classic.spi.IThrowableProxy;
//import hexlet.code.app.dto.TaskStatusDto;
//import hexlet.code.app.model.Label;
//import hexlet.code.app.model.TaskStatus;
//import hexlet.code.app.model.User;
//import com.fasterxml.jackson.core.type.TypeReference;
//import hexlet.code.app.dto.TaskDto;
//import hexlet.code.app.model.Task;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
//
//import static hexlet.code.app.utils.TestUtils.TEST_TASKSTATUS_NAME2;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static hexlet.code.app.utils.TestUtils.TEST_EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
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


//    @Test
//    public void createTask() throws Exception {
//        utils.createDefaultTask();
//        final Task expectedTask = taskRepository.findAll().get(0);
//        final var response = utils.perform(
//                        get("/api/tasks/1",
//                                expectedTask.getId()), TEST_EMAIL)
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//
//        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
//        });
//
//        assertEquals(expectedTask.getName(), task.getName());
//    }
//
//
//    @Test
//    public void updateTaskStatus() throws Exception {
//        utils.createDefaultTask();
//
//        final Long taskId = taskRepository.findAll().get(0).getId();
//        final User user = userRepository.findAll().get(0);
//        final TaskStatus taskStatus = taskStatusRepository.findAll().get(0);
//        final Label label = labelRepository.findAll().get(0);
//        final var newTask = new TaskDto(TEST_TASK_NAME2,
//                TEST_TASK_DESCRIPTION2,
//                user.getId(),
//                taskStatus.getId(),
//                Set.of(label.getId()));
//
//        String currentUrl = "/api" + TASK_CONTROLLER_PATH + TaskStatusController.ID;
//        final var updateRequest = put(currentUrl, taskId)
//                .content(asJson(newTask))
//                .contentType(APPLICATION_JSON);
//        utils.perform(updateRequest, TEST_EMAIL).andExpect(status().isOk());
//
//        assertEquals(taskRepository.findById(taskId).get().getName(), TEST_TASK_NAME2);
//    }


    @Test
    public void deleteTaskStatus() throws Exception {
        utils.createDefaultTask();
        final Long taskId = taskRepository.findAll().get(0).getId();
        String currentUrl = "/api" + TASK_CONTROLLER_PATH + TaskController.ID;
        utils.perform(delete(currentUrl, taskId), TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, taskRepository.count());

    }

}
