package com.Mattheo992.medicalclinic.model;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor source);
    List<DoctorDto> toDtos (List<Doctor> doctors);
    SimpleDoctorDto toSimpleDto(Doctor source);
}