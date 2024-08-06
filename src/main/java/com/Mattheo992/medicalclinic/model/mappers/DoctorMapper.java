package com.Mattheo992.medicalclinic.model.mappers;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.dtos.SimpleDoctorDto;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor source);
    List<DoctorDto> toDtos (List<Doctor> doctors);
    SimpleDoctorDto toSimpleDto(Doctor source);
}