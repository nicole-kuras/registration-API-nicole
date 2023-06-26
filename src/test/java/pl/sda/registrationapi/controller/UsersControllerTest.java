package pl.sda.registrationapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.sda.registrationapi.config.SecurityConfig;
import pl.sda.registrationapi.dto.UserDTO;
import pl.sda.registrationapi.enums.Role;
import pl.sda.registrationapi.service.UsersService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Import(SecurityConfig.class)
@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;


    @BeforeEach
    void reset() {
        Mockito.reset(usersService);
    }

    @Test
    @WithMockUser(roles = {"DOCTOR", "PATIENT"})
    void testGetPageForbidden() throws Exception {
        // given
        var request = MockMvcRequestBuilders.get(UsersController.USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));

        // then
        Mockito.verifyNoInteractions(usersService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetPageHappyPath() throws Exception {
        // given
        List<UserDTO> usersList = Collections.singletonList(UserDTO.builder()
                .id(1L)
                .username("username")
                .password("password")
                .role(Role.ROLE_ADMIN)
                .build());

        Mockito.when(usersService.findAll(Mockito.any(PageRequest.class))).thenReturn(usersList);

        String usersJson = objectMapper.writeValueAsString(usersList);

        var request = MockMvcRequestBuilders.get(UsersController.USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.content().string(usersJson));

        // then
        Mockito.verify(usersService).findAll(Mockito.any(PageRequest.class));
        Mockito.verifyNoMoreInteractions(usersService);
    }


    public static Stream<Arguments> paramsSupplier() {
        return Stream.of(
                Arguments.of("-1", "10", "DESC"),
                Arguments.of("0", "-10", "DESC"),
                Arguments.of("0", "10", "UNKNOWN")

        );
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("paramsSupplier")
    void testGetPageErrorHandlingBadPage(String page, String size, String sortDirection) throws Exception {
        // given
        var request = MockMvcRequestBuilders.get(UsersController.USERS_ENDPOINT)
                .param("page", page)
                .param("size", size)
                .param("sortDirection", sortDirection)
                .param("sortColumn", "id")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));

        // then
        Mockito.verifyNoInteractions(usersService);
    }

    @Test
    @WithMockUser(roles = {"DOCTOR", "PATIENT"})
    void testCreateForbidden() throws Exception {
        // given
        UserDTO userToCreate = UserDTO.builder()
                .id(1L)
                .username("username")
                .password("password")
                .role(Role.ROLE_ADMIN)
                .email("username@gmail.com")
                .build();

        String requestBody = objectMapper.writeValueAsString(userToCreate);

        var request = MockMvcRequestBuilders.post(UsersController.USERS_ENDPOINT)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));

        // then
        Mockito.verifyNoInteractions(usersService);
    }


    public static Stream<Arguments> userDataSupplier() {

        return Stream.of(
                Arguments.of(null, "password", Role.ROLE_ADMIN, "username@gmail.com"),
                Arguments.of("", "password", Role.ROLE_ADMIN, "username@gmail.com"),
                Arguments.of("     ", "password", Role.ROLE_ADMIN, "username@gmail.com"),

                Arguments.of("username", null, Role.ROLE_ADMIN, "username@gmail.com"),
                Arguments.of("username", "", Role.ROLE_ADMIN, "username@gmail.com"),
                Arguments.of("username", "        ", Role.ROLE_ADMIN, "username@gmail.com"),

                Arguments.of("username", "password", null, "username@gmail.com"),

                Arguments.of("username", "password", Role.ROLE_ADMIN, null),
                Arguments.of("username", "password", Role.ROLE_ADMIN, ""),
                Arguments.of("username", "password", Role.ROLE_ADMIN, "          ")
        );
    }


    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("userDataSupplier")
    void testCreateConstrainsViolation(String username, String password, Role role, String email) throws Exception {
        // given
        UserDTO userToCreate = UserDTO.builder()
                .id(1L)
                .username(username)
                .password(password)
                .role(role)
                .email(email)
                .build();

        String requestBody = objectMapper.writeValueAsString(userToCreate);

        var request = MockMvcRequestBuilders.post(UsersController.USERS_ENDPOINT)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));

        // then
        Mockito.verifyNoInteractions(usersService);
    }
}