package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.SpringConfigForIT;
import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
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
import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.app.controller.LabelController.ID;
import static hexlet.code.app.utils.TestUtils.fromJson;
import static hexlet.code.app.utils.TestUtils.asJson;
import static hexlet.code.app.utils.TestUtils.TEST_EMAIL;
import static hexlet.code.app.utils.TestUtils.TEST_LABEL2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class LabelControllerIT {

    @Autowired
    private LabelRepository labelRepository;

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
    public void createLabel() throws Exception {
        LabelDto newLabel = new LabelDto("good_first_issue");

        final var request = post("/api" + LABEL_CONTROLLER_PATH)
                .content(asJson(newLabel)).contentType(APPLICATION_JSON);
        utils.perform(request, TEST_EMAIL).andExpect(status().isCreated());

        final Label expectedLabel = labelRepository.findAll().iterator().next();

        final var response = utils.perform(
                        get("/api" + LABEL_CONTROLLER_PATH + ID,
                                expectedLabel.getId()), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabel.getName(), label.getName());
    }

    @Test
    public void updateLabel() throws Exception {
        utils.createDefaultLabel();

        final Long labelId = labelRepository.findAll().iterator().next().getId();
        final var label = new TaskStatusDto(TEST_LABEL2);

        String currentUrl = "/api" + LABEL_CONTROLLER_PATH + LabelController.ID;
        final var updateRequest = put(currentUrl, labelId)
                .content(asJson(label))
                .contentType(APPLICATION_JSON);
        utils.perform(updateRequest, TEST_EMAIL).andExpect(status().isOk());

        assertEquals(labelRepository.findAll().iterator().next().getName(), TEST_LABEL2);
    }

    @Test
    public void deleteLabel() throws Exception {
        utils.createDefaultLabel();
        final Long labelId = labelRepository.findAll().iterator().next().getId();
        String currentUrl = "/api" + LABEL_CONTROLLER_PATH + LabelController.ID;
        utils.perform(delete(currentUrl, labelId), TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, labelRepository.count());
    }
}
