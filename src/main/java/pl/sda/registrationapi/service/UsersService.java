package pl.sda.registrationapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.registrationapi.dto.UserDTO;
import pl.sda.registrationapi.exception.ConflictException;
import pl.sda.registrationapi.exception.ResourceNotFoundException;
import pl.sda.registrationapi.mapper.UsersMapper;
import pl.sda.registrationapi.model.User;
import pl.sda.registrationapi.repository.UsersRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    public List<UserDTO> findAll(PageRequest pageRequest) {
        log.info("Fetch users page req: '{}'... ", pageRequest);

        List<UserDTO> users = usersRepository.findAll(pageRequest)
                .stream().map(usersMapper::map).toList();

        log.info("Fetching users completed.");
        return users;
    }

    public UserDTO findById(Long id) {
        log.info("Fetch user with id: '{}'...", id);

        User user = getUser(id);

        log.info("Fetching user info completed.");
        return usersMapper.map(user);
    }

    public UserDTO create(UserDTO userDTO) {
        String username = userDTO.getUsername();
        log.info("Creating user with username: '{}'...", username);
        checkIsUsernameAvailable(username);
        User user = encodePassword(userDTO);
        User savedUser = usersRepository.save(user);
        log.info("User create successfully.");
        return usersMapper.map(savedUser);
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        log.info("Updating user with id: '{}'...", id);

        if (!id.equals(userDTO.getId())) {
            throw new ConflictException("Conflict - given ids does not match!");
        }

        User user = getUser(id);

        String updatedUsername = userDTO.getUsername();
        String currentUsername = user.getUsername();

        if (!currentUsername.equals(updatedUsername)) {
            checkIsUsernameAvailable(updatedUsername);
        }

        User userToUpdate = encodePassword(userDTO);
        User updatedUser = usersRepository.save(userToUpdate);

        log.info("Update completed successfully.");
        return usersMapper.map(updatedUser);
    }

    public void deleteById(Long id) {
        log.info("Removing user with id: '{}'...", id);
        throwResourceNotFoundIfUserDoesNotExists(id);
        usersRepository.deleteById(id);
        log.info("User removal completed.");
    }

    public void enable(Long id) {
        log.info("Enabling user with id: '{}'...", id);
        throwResourceNotFoundIfUserDoesNotExists(id);
        usersRepository.enableById(id);
        log.info("User enabled.");
    }

    public void disable(Long id) {
        log.info("Disabling user with id: '{}'...", id);
        throwResourceNotFoundIfUserDoesNotExists(id);
        usersRepository.disableById(id);
        log.info("User disabled.");
    }

    private User getUser(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with given id does not exists!"));
    }

    private User encodePassword(UserDTO userDTO) {
        User user = usersMapper.map(userDTO);
        String encodePass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePass);
        return user;
    }

    private void checkIsUsernameAvailable(String username) {
        boolean usernameExists = usersRepository.existsByUsername(username);

        if (usernameExists) {
            throw new ConflictException("Username already taken!");
        }
    }

    private void throwResourceNotFoundIfUserDoesNotExists(Long id) {
        boolean userExists = usersRepository.existsById(id);

        if (!userExists) {
            throw new ResourceNotFoundException("User with given id does not exists!");
        }
    }
}
