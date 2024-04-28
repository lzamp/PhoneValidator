package service;

import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface PhoneNumberService {
    String validateAndCorrect(String phoneNumber);

    String saveNumbers() throws SQLException;
}
