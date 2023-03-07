package hexlet.code.app.controller;

import hexlet.code.app.model.User;
import hexlet.code.app.dto.UserDto;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.ArrayList;
import java.util.List;
import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

	public static final String USER_CONTROLLER_PATH = "/users";
	private final UserService userService;
	private final UserRepository userRepository;

	@PostMapping(path = "")
	public User createUser(@RequestBody UserDto userDto) {
		return userService.createNewUser(userDto);
	}

	@GetMapping(path = "/{id}")
	public User getUser(@PathVariable Long id) {
		return userRepository.findById(id).get();
	}

	@GetMapping(path = "")
	public List<User> getAllUsers() {
		List<User> allUsers = new ArrayList<>();
		userRepository.findAll().forEach(allUsers::add);
		return allUsers;
	}

	@PutMapping(path = "/{id}")
	public User changeUser(@PathVariable Long id, @RequestBody UserDto userDto) {
		return userService.updateUser(id, userDto);
	}

	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}
