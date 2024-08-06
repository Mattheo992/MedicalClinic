package com.Mattheo992.medicalclinic.model.mappers;

import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.dtos.VisitDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface VisitMapper {
    VisitDto visitDto(Visit source);
    Visit destinationToSource (VisitDto destination);
    List<VisitDto> ListDto (List<Visit> visits);
}