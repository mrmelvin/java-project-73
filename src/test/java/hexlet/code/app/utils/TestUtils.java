package hexlet.code.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.component.JWTHelper;
import hexlet.code.app.dto.StatusDto;
import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.app.controller.StatusController.STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Component
public class TestUtils {

    public static final String TEST_EMAIL = "johndoe@yahoo.com";
    public static final String TEST_EMAIL2 = "jackfrost@hotmail.org";

    public static final String TEST_STATUS_NAME = "newestStatus";

    public static final String TEST_STATUS_NAME2 = "secondStatus";
    private final UserDto testRegistrationDto = new UserDto(
            TEST_EMAIL,
            "john",
            "doe",
            "qwerty"
    );

    private final StatusDto testStatusDto = new StatusDto(TEST_STATUS_NAME);

    public UserDto getTestRegistrationDto() {
        return testRegistrationDto;
    }

    public StatusDto getTestStatusDto() {
        return testStatusDto;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JWTHelper jwtHelper;

    public void tearDown() {
        userRepository.deleteAll();
        statusRepository.deleteAll();
        taskRepository.deleteAll();
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testRegistrationDto);
    }

    public ResultActions regUser(final UserDto dto) throws Exception {
        final var request = post("/api" + USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions createDefaultStatus() throws Exception {
        return createStatus(testStatusDto);
    }

    public ResultActions createStatus(final StatusDto dto) throws Exception {
        final var request = post("/api" + STATUS_CONTROLLER_PATH)
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
