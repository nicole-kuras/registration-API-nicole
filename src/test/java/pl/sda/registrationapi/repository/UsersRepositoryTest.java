package pl.sda.registrationapi.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import pl.sda.registrationapi.enums.Role;
import pl.sda.registrationapi.model.User;

import java.util.Optional;

@DataJpaTest
@Sql(scripts = "classpath:sql/users-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UsersRepositoryTest {

    public static final long ADMIN_ID = 1L;

    @Autowired
    private UsersRepository usersRepository;


    @Test
    void testFindByUsernameNotFound() {
        // given
        final String nonExistingUsername = "non-existing-username";

        // when
        Optional<User> optUser = usersRepository.findByUsername(nonExistingUsername);

        // then
        Assertions.assertFalse(optUser.isPresent());
    }

    @Test
    void testFindByUsernameUserFound() {
        // given
        final String username = "doctor";
        final String email = "doctor@example.com";
        final String passwordHash = "$2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q";

        User expectedUser = User.builder()
                .id(3L)
                .username(username)
                .password(passwordHash)
                .role(Role.ROLE_DOCTOR)
                .enabled(true)
                .email(email)
                .build();

        // when
        Optional<User> optUser = usersRepository.findByUsername(username);

        // then
        Assertions.assertTrue(optUser.isPresent());
        Assertions.assertEquals(expectedUser, optUser.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "doctor", "patient"})
    void testExistsByUsernameTrue(String username) {
        // when
        boolean exists = usersRepository.existsByUsername(username);

        // then
        Assertions.assertTrue(exists);
    }

    @ParameterizedTest
    @ValueSource(strings = {"non-existing-username", "non-existing-username-2"})
    void testExistsByUsernameFalse(String username) {
        // when
        boolean exists = usersRepository.existsByUsername(username);

        // then
        Assertions.assertFalse(exists);
    }

    @Test
    @Sql(scripts = "classpath:sql/users-test.sql",
            statements = "UPDATE users u SET u.enabled = TRUE WHERE u.username = 'admin'")
    void testDisableById() {
        // when
        usersRepository.disableById(ADMIN_ID);

        // then
        User admin = usersRepository.findById(ADMIN_ID).orElse(new User());
        Assertions.assertFalse(admin.isEnabled());
    }

    @Test
    @Sql(scripts = "classpath:sql/users-test.sql",
            statements = "UPDATE users u SET u.enabled = FALSE WHERE u.username = 'admin'")
    void testEnableById() {
        // when
        usersRepository.enableById(ADMIN_ID);

        // then
        User admin = usersRepository.findById(ADMIN_ID).orElse(new User());
        Assertions.assertTrue(admin.isEnabled());
    }

}