package pl.sda.registrationapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sda.registrationapi.dto.UserDTO;
import pl.sda.registrationapi.enums.Role;
import pl.sda.registrationapi.exception.ConflictException;
import pl.sda.registrationapi.exception.ResourceNotFoundException;
import pl.sda.registrationapi.mapper.UsersMapper;
import pl.sda.registrationapi.model.User;
import pl.sda.registrationapi.repository.UsersRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class UsersServiceTest {

    private final UsersMapper usersMapper = new UsersMapper();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UsersRepository usersRepository = Mockito.mock(UsersRepository.class);

    private final UsersService usersService = new UsersService(usersMapper, passwordEncoder, usersRepository);

    @BeforeEach
    void reset() {
        Mockito.reset(usersRepository);
    }

    @Test
    void testFindAll() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);

        User user = getUser();

        UserDTO userDTO1 = usersMapper.map(user);
        List<UserDTO> expectedList = Collections.singletonList(userDTO1);
        Page<User> page = new PageImpl<>(Collections.singletonList(user));
        Mockito.when(usersRepository.findAll(pageRequest)).thenReturn(page);

        // when
        List<UserDTO> actualList = usersService.findAll(pageRequest);

        // then
        Assertions.assertIterableEquals(expectedList, actualList);
        Mockito.verify(usersRepository).findAll(pageRequest);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testFindByIdNotFound() {
        // given
        long nonExistingUserId = Long.MAX_VALUE;

        Mockito.when(usersRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // when
        Executable executable = () -> usersService.findById(nonExistingUserId);

        // then
        Assertions.assertThrows(ResourceNotFoundException.class, executable);
        Mockito.verify(usersRepository).findById(nonExistingUserId);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testFindByIdUserFound() {
        // given
        long userId = 1L;

        User user = getUser();

        UserDTO expectedUserDTO = usersMapper.map(user);
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(usersRepository.findById(userId)).thenReturn(optionalUser);

        // when
        UserDTO actualUserDTO = usersService.findById(userId);

        // then
        Assertions.assertEquals(expectedUserDTO, actualUserDTO);
        Mockito.verify(usersRepository).findById(userId);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testCreateUsernameTaken() {
        // given
        String existingUsername = "existingUsername";

        UserDTO userDTO = UserDTO.builder()
                .username(existingUsername)
                .build();

        Mockito.when(usersRepository.existsByUsername(existingUsername)).thenReturn(true);

        // when
        Executable executable = () -> usersService.create(userDTO);

        // then
        Assertions.assertThrows(ConflictException.class, executable);
        Mockito.verify(usersRepository).existsByUsername(existingUsername);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testCreateSuccess() {
        // given
        String username = "username";

        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .password("password")
                .build();

        User user = usersMapper.map(userDTO);
        String encodePass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePass);

        UserDTO expectedUserDTO = usersMapper.map(user);

        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenReturn(user);

        // when
        UserDTO actualUserDTO = usersService.create(userDTO);

        // then
        Assertions.assertEquals(expectedUserDTO, actualUserDTO);

        Mockito.verify(usersRepository).existsByUsername(username);
        Mockito.verify(usersRepository).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(usersRepository);
    }


    @Test
    void testUpdateIdsConflict() {
        // given
        long id = 1L;

        UserDTO userDTO = UserDTO.builder()
                .id(2L)
                .username("username")
                .password("password")
                .build();

        // when
        Executable executable = () -> usersService.update(id, userDTO);

        // then
        Assertions.assertThrows(ConflictException.class, executable);
        Mockito.verifyNoInteractions(usersRepository);
    }

    @Test
    void testUpdateUserNotFound() {
        // given
        long id = 1L;
        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .username("username")
                .password("password")
                .build();

        Mockito.when(usersRepository.findById(id)).thenReturn(Optional.empty());

        // when
        Executable executable = () -> usersService.update(id, userDTO);

        // then
        Assertions.assertThrows(ResourceNotFoundException.class, executable);
        Mockito.verify(usersRepository).findById(id);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testUpdateUsernameChanged() {
        // given
        long id = 1L;

        String oldUsername = "oldUsername";
        String newUsername = "newUsername";

        User user = User.builder()
                .id(id)
                .username(oldUsername)
                .password("password")
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .username(newUsername)
                .password("password")
                .build();

        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(usersRepository.findById(id)).thenReturn(optionalUser);
        Mockito.when(usersRepository.existsByUsername(newUsername)).thenReturn(true);

        // when
        Executable executable = () -> usersService.update(id, userDTO);

        // then
        Assertions.assertThrows(ConflictException.class, executable);
        Mockito.verify(usersRepository).findById(id);
        Mockito.verify(usersRepository).existsByUsername(newUsername);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }


    @Test
    void testUpdateSuccess() {
        // given
        long id = 1L;

        String username = "username";
        User user = User.builder()
                .id(id)
                .username(username)
                .password("password")
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .username(username)
                .password("password")
                .build();

        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(usersRepository.findById(id)).thenReturn(optionalUser);

        String encodePass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePass);

        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDTO expectedUserDTO = usersMapper.map(user);

        // when
        UserDTO actualUserDTO = usersService.update(id, userDTO);

        // then
        Assertions.assertEquals(expectedUserDTO, actualUserDTO);

        Mockito.verify(usersRepository).findById(id);
        Mockito.verify(usersRepository).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(usersRepository);
    }


    @Test
    void testDeleteByIdHappyPath() {
        // given
        long id = 1L;
        Mockito.when(usersRepository.existsById(id)).thenReturn(true);

        // when
        usersService.deleteById(id);

        // then
        Mockito.verify(usersRepository).existsById(id);
        Mockito.verify(usersRepository).deleteById(id);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testDeleteByIdUserNotFound() {
        // given
        long id = 1L;
        Mockito.when(usersRepository.existsById(id)).thenReturn(false);

        // when
        Executable executable = () -> usersService.deleteById(id);

        // then
        Assertions.assertThrows(ResourceNotFoundException.class, executable);
        Mockito.verify(usersRepository).existsById(id);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testEnableHappyPath() {
        // given
        long id = 1L;
        Mockito.when(usersRepository.existsById(id)).thenReturn(true);

        // when
        usersService.enable(id);

        // then
        Mockito.verify(usersRepository).existsById(id);
        Mockito.verify(usersRepository).enableById(id);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testEnableUserNotFound() {
        // given
        long id = 1L;
        Mockito.when(usersRepository.existsById(id)).thenReturn(false);

        // when
        Executable executable = () -> usersService.enable(id);

        // then
        Assertions.assertThrows(ResourceNotFoundException.class, executable);
        Mockito.verify(usersRepository).existsById(id);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testDisableHappyPath() {
        // given
        long id = 1L;
        Mockito.when(usersRepository.existsById(id)).thenReturn(true);

        // when
        usersService.disable(id);

        // then
        Mockito.verify(usersRepository).existsById(id);
        Mockito.verify(usersRepository).disableById(id);
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testDisableUserNotFound() {
        // given
        long id = 1L;
        Mockito.when(usersRepository.existsById(id)).thenReturn(false);

        // when
        Executable executable = () -> usersService.disable(id);

        // then
        Assertions.assertThrows(ResourceNotFoundException.class, executable);

    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .role(Role.ROLE_DOCTOR)
                .enabled(true)
                .email("email")
                .build();
    }
}