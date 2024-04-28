package dao;


import entity.DataEntry;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public interface PhoneNumberDAO {
    public List<DataEntry> extractPhoneNumber(Date dateLoad) throws SQLException;

    Date extractLastDate()  throws SQLException;
}
