package com.Mattheo992.medicalclinic.model;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VisitMapper {
    VisitDto visitDto(Visit source);
    Visit destinationToSource (VisitDto destination);
}