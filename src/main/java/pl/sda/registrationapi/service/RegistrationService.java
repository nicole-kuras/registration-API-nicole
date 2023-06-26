package pl.sda.registrationapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sda.registrationapi.dto.*;
import pl.sda.registrationapi.enums.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UsersService usersService;
    private final DoctorsService doctorsService;
    private final PatientsService patientsService;

    @Transactional
    public void registerPatient(PatientSignupDTO patientSignupDTO) {
        log.info("Patient registration process started...");
        UserDTO createdUser = createUser(
                Role.ROLE_PATIENT,
                patientSignupDTO.getUsername(),
                patientSignupDTO.getPassword(),
                patientSignupDTO.getEmail()
        );

        PatientDTO patientDTO = PatientDTO.builder()
                .name(patientSignupDTO.getName())
                .surname(patientSignupDTO.getSurname())
                .pesel(patientSignupDTO.getPesel())
                .dateOfBirth(patientSignupDTO.getDateOfBirth())
                .phoneNumber(patientSignupDTO.getPhoneNumber())
                .build();

        patientsService.create(createdUser.getId(), patientDTO);
        log.info("Patient registration process completed.");
    }

    @Transactional
    public void registerDoctor(DoctorSignupDTO doctorSignupDTO) {
        log.info("Doctor registration process started...");
        UserDTO createdUser = createUser(
                Role.ROLE_DOCTOR,
                doctorSignupDTO.getUsername(),
                doctorSignupDTO.getPassword(),
                doctorSignupDTO.getEmail()
        );

        DoctorDTO doctorDTO = DoctorDTO.builder()
                .title(doctorSignupDTO.getTitle())
                .specialization(doctorSignupDTO.getSpecialization())
                .name(doctorSignupDTO.getName())
                .surname(doctorSignupDTO.getSurname())
                .dateOfBirth(doctorSignupDTO.getDateOfBirth())
                .phoneNumber(doctorSignupDTO.getPhoneNumber())
                .build();

        doctorsService.create(createdUser.getId(), doctorDTO);
        log.info("Doctor registration process completed.");
    }


    private UserDTO createUser(Role role, String username, String password, String email) {
        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .enabled(true)
                .build();

        return usersService.create(userDTO);
    }
}
