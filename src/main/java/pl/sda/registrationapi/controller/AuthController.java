package pl.sda.registrationapi.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.sda.registrationapi.dto.LoginDTO;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
@RequestMapping(AuthController.AUTH_ENDPOINT)
public class AuthController {

    public final static String AUTH_ENDPOINT = "/auth";

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Authentication auth = authenticationManager.authenticate(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}
