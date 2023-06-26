package pl.sda.registrationapi.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.sda.registrationapi.dto.PatientDTO;
import pl.sda.registrationapi.model.Patient;

@Mapper
public interface PatientsMapper {

    PatientsMapper INSTANCE = Mappers.getMapper(PatientsMapper.class);

    Patient map(PatientDTO patientDTO);

    PatientDTO map(Patient patient);
}
