package pl.sda.registrationapi.mapper;


import org.springframework.stereotype.Component;
import pl.sda.registrationapi.dto.UserDTO;
import pl.sda.registrationapi.model.User;

@Component
public class UsersMapper {

    public User map(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .enabled(userDTO.isEnabled())
                .email(userDTO.getEmail())
                .build();
    }

    public UserDTO map(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .email(user.getEmail())
                .build();
    }
}
