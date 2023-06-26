package pl.sda.registrationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.sda.registrationapi.dto.DoctorDTO;
import pl.sda.registrationapi.service.DoctorsService;
import pl.sda.registrationapi.utils.PageReqUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Doctors Controller")
@RequestMapping(DoctorsController.DOCTORS_ENDPOINT)
public class DoctorsController {

    public final static String DOCTORS_ENDPOINT = "/api/doctors";

    private final DoctorsService doctorsService;

    @GetMapping
    @Operation(summary = "Method is being used to get doctors page - depends on given params.")
    public ResponseEntity<List<DoctorDTO>> getPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(required = false) String sortColumn) {

        PageRequest pageReq = PageReqUtils.createPageReq(page, size, sortDirection, sortColumn);
        return ResponseEntity.ok(doctorsService.findAll(pageReq));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Method is being used to get information about doctor with given id.")
    public ResponseEntity<DoctorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorsService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Method is being used to update doctor with given id.")
    public void updateById(@PathVariable Long id, @Valid @RequestBody DoctorDTO doctorDTO) {
        doctorsService.update(id, doctorDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Method is being used to delete doctor with given id.")
    public void delete(@PathVariable Long id) {
        doctorsService.deleteById(id);
    }
}
