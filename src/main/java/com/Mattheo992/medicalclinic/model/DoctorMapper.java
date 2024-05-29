package com.Mattheo992.medicalclinic.model;

import com.Mattheo992.medicalclinic.model.DoctorDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper(componentModel = "spring", implementationName = "CustomDoctorMapperImpl")
public interface DoctorMapper {
    DoctorDto sourceToDestination(Doctor source);
    List<DoctorDto> sourceToDestination(List<Doctor> source);
}