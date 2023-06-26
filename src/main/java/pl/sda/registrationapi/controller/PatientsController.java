package pl.sda.registrationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.registrationapi.dto.PatientDTO;
import pl.sda.registrationapi.service.PatientsService;
import pl.sda.registrationapi.utils.PageReqUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Patients Controller")
@RequestMapping(PatientsController.PATIENTS_ENDPOINT)
public class PatientsController {

    public final static String PATIENTS_ENDPOINT = "/api/patients";

    private final PatientsService patientsService;

    @GetMapping
    @Operation(summary = "Method is being used to get patients page - depends on given params.")
    public ResponseEntity<List<PatientDTO>> getPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(required = false) String sortColumn) {

        PageRequest pageReq = PageReqUtils.createPageReq(page, size, sortDirection, sortColumn);
        return ResponseEntity.ok(patientsService.findAll(pageReq));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Method is being used to get information about patient with given id.")
    public ResponseEntity<PatientDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientsService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Method is being used to update patient with given id.")
    public void updateById(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        patientsService.update(id, patientDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Method is being used to delete patient with given id.")
    public void delete(@PathVariable Long id) {
        patientsService.deleteById(id);
    }
}
