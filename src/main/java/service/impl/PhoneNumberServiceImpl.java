package service.impl;


import DTO.PhoneNumberValidationResult;
import dao.PhoneNumberDAO;
import domain.PhoneNumber;
import entity.DataEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.DataEntryRepository;
import repository.PhoneNumberRepository;
import service.PhoneNumberService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    @Autowired
    private PhoneNumberDAO phoneNumberDAO;

    @Autowired
    private DataEntryRepository dataEntryRepository;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Override
    public PhoneNumberValidationResult validateAndCorrect(String phoneNumber) {
        // Si assume da requisito che il numero corretto ha lunghezza 11 e inizia con 27.
        //Vado a verificare che il numero non sia null o vuoto. In questo caso vado a mettere null nel campo del numero di telefono
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return new PhoneNumberValidationResult(null, "INVALID");
        }

        //Rimuove eventuali spazzi
        phoneNumber = phoneNumber.replaceAll("\\s+", "");

        // Rimuovi tutti i caratteri non numerici eccetto il segno + all'inizio se presente
        phoneNumber = phoneNumber.replaceAll("[^\\d]", "");

        // Va a vedere se il numero inizia con 27 e se la lunghezza è 11.
        if (phoneNumber.length() == 11 && phoneNumber.startsWith("27")) {
            return new PhoneNumberValidationResult(phoneNumber, "ACCEPTABLE");
        } else if (phoneNumber.length() == 9 && !phoneNumber.startsWith("27")) {
            // Se il prefisso 27 é assente lo va ad aggiungere.
            phoneNumber = "27" + phoneNumber;
            return new PhoneNumberValidationResult(phoneNumber, "CORRECTED");
        }

        return new PhoneNumberValidationResult(null, "INVALID");
    }


    @Override
    @Transactional
    public boolean saveNumbers() throws SQLException {
        Date date = phoneNumberDAO.extractLastDate();
        List<DataEntry> dataEntries = phoneNumberDAO.extractPhoneNumber(date);
        boolean ris = false;

        if(!dataEntries.isEmpty()){
            for (DataEntry entry : dataEntries) {
                PhoneNumberValidationResult validationResult = validateAndCorrect(entry.getPhoneNumber());
                if (validationResult.getStatus().equals("ACCEPTABLE") || validationResult.getStatus().equals("CORRECTED")) {
                    PhoneNumber phoneNumber = new PhoneNumber(validationResult.getPhoneNumber(), validationResult.getStatus(), LocalDate.now());
                    phoneNumberRepository.save(phoneNumber);
                    ris = true;
                } else {
                    ris = false;
                }
            }
        }
        return ris;
    }
}
