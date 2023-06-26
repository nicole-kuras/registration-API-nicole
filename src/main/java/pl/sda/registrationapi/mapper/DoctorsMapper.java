package pl.sda.registrationapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.sda.registrationapi.dto.DoctorDTO;
import pl.sda.registrationapi.model.Doctor;

@Mapper
public interface DoctorsMapper {

    DoctorsMapper INSTANCE = Mappers.getMapper(DoctorsMapper.class);

//    @Mapping(target = "userId", source = "user.id")
//    @Mapping(target = "phoneNo", source = "phoneNumber")
    DoctorDTO map(Doctor doctor);

    Doctor map(DoctorDTO doctorDTO);
}
