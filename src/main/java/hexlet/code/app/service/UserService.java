package hexlet.code.app.service;

import hexlet.code.app.model.User;
import hexlet.code.app.dto.UserDto;

public interface UserService {
	User createNewUser(UserDto userDto);

	User updateUser(long id, UserDto userDto);

	String getCurrentUserName();

	User getCurrentUser();

}
