package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.controller.TaskStatusController;
import hexlet.code.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Set;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Component
public class TestUtils {

    public static final String TEST_EMAIL = "johndoe@yahoo.com";
    public static final String TEST_EMAIL2 = "jackfrost@hotmail.org";

    public static final String TEST_TASKSTATUS_NAME = "newestStatus";

    public static final String TEST_TASKSTATUS_NAME2 = "secondStatus";

    public static final String TEST_LABEL = "good first issue";
    public static final String TEST_LABEL2 = "wontfix";

    public static final String TEST_TASK_NAME = "First_Test_Task";
    public static final String TEST_TASK_NAME2 = "Second_Test_Task";
    public static final String TEST_TASK_DESCRIPTION = "Lorem_Ipsum";
    public static final String TEST_TASK_DESCRIPTION2 = "Sed ut perspiciatis unde omnis iste natus";


    private final UserDto testRegistrationDto = new UserDto(
            TEST_EMAIL,
            "john",
            "doe",
            "qwerty"
    );

    private final TaskStatusDto testTaskStatusDto = new TaskStatusDto(TEST_TASKSTATUS_NAME);
    private final LabelDto testLabelDto = new LabelDto(TEST_LABEL);


    public UserDto getTestRegistrationDto() {
        return testRegistrationDto;
    }

    public TaskStatusDto getTestStatusDto() {
        return testTaskStatusDto;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private JWTHelper jwtHelper;

    public void tearDown() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testRegistrationDto);
    }

    public ResultActions regUser(final UserDto dto) throws Exception {
        final var request = MockMvcRequestBuilders.post("/api" + UserController.USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions createDefaultTaskStatus() throws Exception {
        return createTaskStatus(testTaskStatusDto);
    }
    public ResultActions createDefaultLabel() throws Exception {
        return createLabel(testLabelDto);
    }

    public ResultActions createDefaultTask() throws Exception {
        regDefaultUser();
        createDefaultTaskStatus();
        createDefaultLabel();
        final User user = userRepository.findAll().get(0);
        final TaskStatus taskStatus = taskStatusRepository.findAll().get(0);
        final Label label = labelRepository.findAll().get(0);
        final TaskDto dto = new TaskDto(TEST_TASK_NAME,
                                        TEST_TASK_DESCRIPTION,
                                        user.getId(),
                                        taskStatus.getId(),
                                        Set.of(label.getId()));
        return createTask(dto);
    }

    public ResultActions createTaskStatus(final TaskStatusDto dto) throws Exception {
        final var request = MockMvcRequestBuilders.post("/api" + TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
                                                            .content(asJson(dto))
                                                            .contentType(APPLICATION_JSON);
        return perform(request, TEST_EMAIL);
    }

    public ResultActions createLabel(final LabelDto dto) throws Exception {
        final var request = post("/api" + LABEL_CONTROLLER_PATH)
                                                            .content(asJson(dto))
                                                            .contentType(APPLICATION_JSON);
        return perform(request, TEST_EMAIL);
    }

    public ResultActions createTask(final TaskDto dto) throws Exception {
        final var request = post("/api" + TASK_CONTROLLER_PATH)
                                                        .content(asJson(dto))
                                                        .contentType(APPLICATION_JSON);
        return perform(request, TEST_EMAIL);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

}
