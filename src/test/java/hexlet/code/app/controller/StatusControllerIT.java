package hexlet.code.app.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.StatusDto;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static hexlet.code.app.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.app.controller.StatusController.ID;
import static hexlet.code.app.utils.TestUtils.asJson;
import static hexlet.code.app.utils.TestUtils.fromJson;
import static hexlet.code.app.utils.TestUtils.TEST_EMAIL;
import static hexlet.code.app.utils.TestUtils.TEST_STATUS_NAME2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hexlet.code.app.controller.StatusController.STATUS_CONTROLLER_PATH;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class StatusControllerIT {

    @Autowired
    private StatusRepository statusRepository;

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
    public void createStatus() throws Exception {
        StatusDto newStatus = new StatusDto("testNewStatus");
        final var request = post("/api" + STATUS_CONTROLLER_PATH)
                                                            .content(asJson(newStatus)).contentType(APPLICATION_JSON);
        utils.perform(request, TEST_EMAIL).andExpect(status().isCreated());

        final Status expectedStatus = statusRepository.findAll().iterator().next();
        final var response = utils.perform(
                        get("/api" + STATUS_CONTROLLER_PATH + ID,
                                expectedStatus.getId()), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Status status = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedStatus.getName(), status.getName());
    }


    @Test
    public void updateTask() throws Exception {
        utils.createDefaultStatus();

        final Long statusId = statusRepository.findAll().iterator().next().getId();
        final var status = new StatusDto(TEST_STATUS_NAME2);

        String currentUrl = "/api" + STATUS_CONTROLLER_PATH + StatusController.ID;
        final var updateRequest = put(currentUrl, statusId)
                .content(asJson(status))
                .contentType(APPLICATION_JSON);
        utils.perform(updateRequest, TEST_EMAIL).andExpect(status().isOk());

        assertEquals(statusRepository.findAll().iterator().next().getName(), TEST_STATUS_NAME2);
    }


    @Test
    public void deleteTask() throws Exception {
        utils.createDefaultStatus();
        final Long statusId = statusRepository.findAll().iterator().next().getId();
        String currentUrl = "/api" + STATUS_CONTROLLER_PATH + StatusController.ID;
        utils.perform(delete(currentUrl, statusId), TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, statusRepository.count());

    }
}
