package service.impl;


import dao.PhoneNumberDAO;
import entity.DataEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DataEntryRepository;
import service.PhoneNumberService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    @Autowired
    private PhoneNumberDAO phoneNumberDAO;

    @Autowired
    private DataEntryRepository dataEntryRepository;

    @Override
    public String validateAndCorrect(String phoneNumber) {
        // Si assume da requisito che il numero corretto ha lunghezza 11 e inizia con 27
        if (phoneNumber == null) {
            return null;
        }
        phoneNumber = phoneNumber.replaceAll("\\s+", ""); // remove white spaces

        // Initial check for length and prefix
        if (phoneNumber.length() == 11 && phoneNumber.startsWith("27")) {
            return phoneNumber;
        } else if (phoneNumber.length() == 9 && !phoneNumber.startsWith("27")) {
            // If '27' prefix is missing, attempt to correct it
            phoneNumber = "27" + phoneNumber;
            return phoneNumber;
        }
        // Invalid or uncorrectable number
        return null;
    }


    @Override
    public String saveNumbers() throws SQLException {
        Date date = phoneNumberDAO.extractLastDate();
        List<DataEntry> dataEntries = phoneNumberDAO.extractPhoneNumber(date);
        if(!dataEntries.isEmpty()){
            for (DataEntry entry : dataEntries) {
                String correctedPhoneNumber = validateAndCorrect(entry.getPhoneNumber());
                if (correctedPhoneNumber != null) {
                    entry.setPhoneNumber(correctedPhoneNumber);
                    entry.set
                    dataEntryRepository.save(entry);  // Save the corrected entry
                } else {
                    // Handle invalid phone numbers, e.g., log them or send them to a separate process
                }
            }
        }
        return null;
    }
}
