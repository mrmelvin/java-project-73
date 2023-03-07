package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import static hexlet.code.app.config.security.SecurityConfig.DEFAULT_AUTHORITIES;


@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User createNewUser(UserDto userDto) {
		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return userRepository.save(user);
	}


	@Override
	public User updateUser(long id, UserDto userDto) {
		User userToUpdate = userRepository.findById(id).get();
		userToUpdate.setFirstName(userDto.getFirstName());
		userToUpdate.setLastName(userDto.getLastName());
		userToUpdate.setEmail(userDto.getEmail());
		userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return userRepository.save(userToUpdate);
	}


	@Override
	public String getCurrentUserName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@Override
	public User getCurrentUser() {
		return userRepository.findByEmail(getCurrentUserName()).get();
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username)
				.map(this::buildSpringUser)
				.orElseThrow(() -> new UsernameNotFoundException("Not found user with 'username': " + username));
	}

	private UserDetails buildSpringUser(final User user) {
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				DEFAULT_AUTHORITIES
		);
	}

}
