package dao.impl;

import dao.PhoneNumberDAO;
import entity.DataEntry;
import entity.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PhoneNumberDAOImpl implements PhoneNumberDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PhoneNumberDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DataEntry> extractPhoneNumberDate(Date dateLoad) throws SQLException {
        List<DataEntry> result = null;
        String sql = "SELECT * FROM daticsv d where date_load > ?";
        Object[] params = {dateLoad};

        result = jdbcTemplate.query(sql, params, (resultSet, rowNum) -> {
            DataEntry ris = new DataEntry();
            ris.setId(resultSet.getString("id"));
            ris.setPhoneNumber(resultSet.getString("phoneNumber"));
            ris.setDateLoad(resultSet.getDate("dateLoad"));
            return ris;

        });
        return result;
        }

    @Override
    public List<DataEntry> extractPhoneNumber() throws SQLException {
        List<DataEntry> result = null;
        String sql = "SELECT * FROM daticsv";

        result = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            DataEntry ris = new DataEntry();
            ris.setId(resultSet.getString("id"));
            ris.setPhoneNumber(resultSet.getString("phone_number"));
            ris.setDateLoad(resultSet.getDate("date_load"));
            return ris;

        });
        return result;
    }

    @Override
    public Date extractLastDate() throws SQLException {

        String sql = "SELECT date_load FROM phone_numbers order by date_load desc LIMIT 1";
        Date date = jdbcTemplate.queryForObject(sql, Date.class);

        return date;
    }

    @Override
    public List<DataEntry> extractPhoneNumberInput(String phoneNumber) throws SQLException {
        List<DataEntry> result = null;
        String sql = "SELECT * FROM daticsv where phone_number = ?";
        Object[] params = {phoneNumber};
        result = jdbcTemplate.query(sql, params,(resultSet, rowNum) -> {
            DataEntry ris = new DataEntry();
            ris.setId(resultSet.getString("id"));
            ris.setPhoneNumber(resultSet.getString("phone_number"));
            ris.setDateLoad(resultSet.getDate("date_load"));
            return ris;

        });
        return result;
    }

    @Override
    public List<PhoneNumber> extractElaboratedNumber(String phoneNum) throws SQLException {

        List<PhoneNumber> result = null;
        String sql = "SELECT p.phone_number, p.status FROM phone_numbers p inner join daticsv d on d.id = p.id where d.phone_number = ? order by p.date_load desc";
        Object[] params = {phoneNum};
        result = jdbcTemplate.query(sql, params,(resultSet, rowNum) -> {
            PhoneNumber ris = new PhoneNumber();
            ris.setPhoneNumber(resultSet.getString("phone_number"));
            ris.setStatus(resultSet.getString("status"));
            return ris;

        });
        return result;
    }

    @Override
    @Transactional
    public boolean saveNumber(String phoneNum) throws SQLException {
        // Recupera l'ID massimo corrente dalla tabella
        String maxIdSql = "SELECT MAX(id) FROM daticsv ";
        Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class);

        if (maxId == null) {
            maxId = 0;  // se non ci sono record, inizia da 1
        }
        int newId = maxId + 1;

        // Inserisci il nuovo record con l'ID incrementato
        String insertSql = "INSERT INTO daticsv (id, phone_number, date_load) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(insertSql, newId, phoneNum, new java.sql.Date(System.currentTimeMillis()));

        return rowsAffected > 0; // restituisce true se l'inserimento è riuscito
    }



}
