package pl.sda.registrationapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.sda.registrationapi.dto.UserDTO;
import pl.sda.registrationapi.service.UsersService;
import pl.sda.registrationapi.utils.PageReqUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users Controller")
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(UsersController.USERS_ENDPOINT)
public class UsersController {

    public final static String USERS_ENDPOINT = "/api/users";

    // HTTP Methods
    // =============================
    // GET -> pobieramy zasoby  200 OK
    // POST -> tworzymy zasoby  201 CREATED
    // PUT -> całościowy update zasobów 201 CREATED
    // PATCH (Łatka) -> częściowy update zasobu 201 CREATED
    // DELETE -> usuwanie zasobów 204 NO_CONTENT
    // HEAD -> pobranie nagłówków
    // OPTIONS -> pobiera opcje, które mogą być wykonane na konkretnym zasobie

    private final UsersService usersService;

    // PathVariable - http://localhost:9090/api/users/1
    // RequestParam - http://localhost:9090/api/users?page=0&size=10&sortDirection=DESC&sortColumn=email

    @GetMapping
    public ResponseEntity<List<UserDTO>> getPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(required = false) String sortColumn) {

        PageRequest pageReq = PageReqUtils.createPageReq(page, size, sortDirection, sortColumn);
        return ResponseEntity.ok(usersService.findAll(pageReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usersService.create(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateById(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(usersService.update(id, userDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        usersService.deleteById(id);
    }

    @PatchMapping("/{id}/disable")
    @ResponseStatus(HttpStatus.OK)
    public void disableById(@PathVariable Long id) {
        usersService.disable(id);
    }

    @PatchMapping("/{id}/enable")
    @ResponseStatus(HttpStatus.OK)
    public void enableById(@PathVariable Long id) {
        usersService.enable(id);
    }
}
