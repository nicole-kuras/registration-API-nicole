package pl.sda.registrationapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import pl.sda.registrationapi.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username must not be blank!")
    private String username;

    @NotBlank(message = "Password must not be blank!")
    private String password;

    @NotNull(message = "Role cannot be null!")
    private Role role;

    private boolean enabled;

    @NotBlank(message = "Email must not be blank!")
    private String email;
}
