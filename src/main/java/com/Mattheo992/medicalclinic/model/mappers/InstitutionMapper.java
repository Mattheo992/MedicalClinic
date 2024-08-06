package com.Mattheo992.medicalclinic.model.mappers;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.model.dtos.SimpleDoctorDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface InstitutionMapper {
    InstitutionDto toDto(Institution source);
    List<InstitutionDto> toListDtos(List<Institution> source);

    Institution toEntity(InstitutionDto source);
    List<Institution> toEntity(List<InstitutionDto> source);

    List<SimpleDoctorDto> toSimpleDoctorDto(List<Doctor> source);
}
