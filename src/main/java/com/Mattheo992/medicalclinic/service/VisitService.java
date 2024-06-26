package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.dtos.VisitDto;
import com.Mattheo992.medicalclinic.model.mappers.VisitMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final VisitMapper visitMapper;

    //Case 1: W przypadku gdy wywołany przez metodę findById z patientRepository zwraca pustego Optional to powinno rzucić
    // wyjątkiem, że nie znaleziono pacjenta o podanym Id.
    //Case 2: W przypadku gdy wywołany przez metodę findById z patientRepository zwraca Optional<Patient>, metoda
    // findByPatientId z visitRepository przekazuje listę wizyt dla przekazanego pacjenta jeśli ten posiada przypisane wizyty,
    // a następnie lista jest mapowana i zwracana jako List<VisitDto>
    //Case 3: W przypadku gdy wywołany przez metodę findById z patientRepository zwraca Optional<Patient>, metoda
    // findByPatientId z visitRepository przekazuje pustą listę wizyt dla przekazanego pacjenta jeśli ten nie posiada
    // przypisanych wizyt.
    public List<VisitDto> getVisitsByPatientId(Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        List<Visit> visits = visitRepository.findByPatientId(patientId);
        return visitMapper.ListDto(visits);
    }

    //    Case 1: W przypadku wywołania metody findById z visitRepository zostanie zwrócony pusty Optional to metoda rzuca wyjątkiem,
//    ze wizyta o podanym Id nie istnieje.
//            Case 2:  W przypadku wywołania metody findById z visitRepository zostanie zwrócony Optional<Visit> metoda
//            getPatient zwróci pacjenta to rzucany jest wyjątek, wyzita jest już zajęta.
//
//            Case 3:  W przypadku wywołania metody findById z visitRepository zostanie zwrócony Optional<Visit> metoda
//            getPatient zwróci null,  metoda findById z patientRepository zwraca pustego Optional, rzucany jest wyjątek,
//            że pacjent o podanym id nie istnieje.
//
//            Case 4:  W przypadku wywołania metody findById z visitRepository zostanie zwrócony Optional<Visit> metoda
//            getPatient zwróci null,  metoda findById z patientRepository zwraca Optional<Patient> to pacjent jest
//            przypisywany do wizyty. Następnie wizyta zostaje zapisana na podstawie metody save z visitRepository.
    @Transactional
    public Visit registerPatientForVisit(Long visitId, Long patientId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit with given id does not exist."));
        if (visit.getPatient() != null) {
            throw new IllegalArgumentException("Sorry, but visit is already occupied.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given id does not exist."));
        visit.setPatient(patient);
        return visitRepository.save(visit);
    }

    //createVisit
    //
    //Case 1: Po wywołaniu metody checkIsStartDateIsAvailable zwraca wartość true to rzuca wyjątek, że wizyta o takiej
    // godzinie rozpoczęcia już istnieje.
    //Case 2: Po wywołaniu metody checkIsStartDateIsAvailable zwraca wartość false, a data startu wizyty porównana
    // do bieżącej daty wskazuje, że jest ona w przeszłości rzucany jest wyjątek, że data startu wizyty musi być w przyszłości.
    //Case 3: Po wywołaniu metody checkIsStartDateIsAvailable zwraca wartość false,  data startu wizyty porównana
    // do bieżącej daty wskazuje, że jest ona w przyszłości, a minuty rozpoczęcia wizyty nie są wielokrotnością 15
    // to rzucany jest wyjątek, że minuty muszą być wielokrotnością 15.
    //Case 4: Po wywołaniu metody checkIsStartDateIsAvailable zwraca wartość false,
    // data startu wizyty porównana do bieżącej daty wskazuje, że jest ona w przyszłości, a minuty rozpoczęcia wizyty
    // są wielokrotnością 15 to wizyta zostaje utworzona i przy pomocy metody save z visitRepository zostaje zapisana.
    @Transactional
    public Visit createVisit(VisitDto visitDto) {
        checkIsStartDateIsAvailable(visitDto.getStartDate());
        LocalDateTime startDate = visitDto.getStartDate();
        LocalDateTime endDate = visitDto.getEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isBefore(now)) {
            throw new IllegalArgumentException("Start date must be in the future.");
        }
        if (startDate.getMinute() % 15 != 0 || endDate.getMinute() % 15 != 0) {
            throw new IllegalArgumentException("Minutes must be multiples of 15.");
        }
        Visit visit = new Visit();
        visit.setStartDate(startDate);
        visit.setEndDate(endDate);
        return visitRepository.save(visit);
    }

    private void checkIsStartDateIsAvailable(LocalDateTime startDate) {
        if (visitRepository.existsByStartDate(startDate)) {
            throw new IllegalArgumentException("Visit with given date is already exist");
        }
    }
}