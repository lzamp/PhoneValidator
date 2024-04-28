package dao.impl;

import dao.PhoneNumberDAO;
import entity.DataEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public List<DataEntry> extractPhoneNumber(Date dateLoad) throws SQLException {
        List<DataEntry> result = null;
        String sql = "SELECT * FROM datiCSV d where dateLoad > ?";
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
    public Date extractLastDate() throws SQLException {

        String sql = "SELECT top 1 dateLoad FROM phoneNumbers d order by dateLoad desc";
        Date date = jdbcTemplate.queryForObject(sql, Date.class);

        return date;
    }


}
