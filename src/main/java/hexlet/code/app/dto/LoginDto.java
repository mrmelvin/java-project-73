package hexlet.code.app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

	private String firstName;

	private String lastName;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 3, max = 100)
	private String password;

}
