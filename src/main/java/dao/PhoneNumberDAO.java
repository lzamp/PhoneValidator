package dao;


import entity.DataEntry;
import entity.PhoneNumber;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public interface PhoneNumberDAO {
    public List<DataEntry> extractPhoneNumber() throws SQLException;
    public List<DataEntry> extractPhoneNumberDate(Date dateLoad) throws SQLException;
    Date extractLastDate()  throws SQLException;
    List<DataEntry> extractPhoneNumberInput(String phoneNumber) throws SQLException;

    List<PhoneNumber> extractElaboratedNumber(String phoneNum) throws SQLException;

    boolean saveNumber(String phoneNum) throws SQLException;
}
