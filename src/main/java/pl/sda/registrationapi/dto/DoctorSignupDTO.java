package pl.sda.registrationapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSignupDTO {

    @NotBlank(message = "Username must not be blank!")
    private String username;

    @NotBlank(message = "Password must not be blank!")
    private String password;

    @NotBlank(message = "Email must not be blank!")
    private String email;

    @Length(max = 50, message = "Title max length exceeded (max = 50)!")
    private String title;

    @NotBlank(message = "Specialization must not be blank!")
    private String specialization;

    @NotBlank(message = "Name must not be blank!")
    @Length(max = 50, message = "Name max length exceeded (max = 50)!")
    private String name;

    @NotBlank(message = "Surname must not be blank!")
    @Length(max = 50, message = "Name max length exceeded (max = 50)!")
    private String surname;

    @NotNull(message = "Date of birth must not be blank!")
    @PastOrPresent(message = "Date of birth must not be in past or present!")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number must not be blank!")
    @Length(max = 15, message = "Name max length exceeded (max = 15)!")
    private String phoneNumber;
}
