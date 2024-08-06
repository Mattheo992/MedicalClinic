package com.Mattheo992.medicalclinic.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}