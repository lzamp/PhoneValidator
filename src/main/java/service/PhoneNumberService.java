package service;

import DTO.PhoneNumberValidationResult;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface PhoneNumberService {
    PhoneNumberValidationResult validateAndCorrect(String phoneNumber);

    boolean saveNumbers() throws SQLException;
}
