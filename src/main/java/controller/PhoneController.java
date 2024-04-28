package controller;

import com.opencsv.CSVReader;
import entity.DataEntry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import repository.DataEntryRepository;
import service.PhoneNumberService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PhoneController {
    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private DataEntryRepository dataEntryRepo;

    //Chiamata post che prende in input un file csv con una lista di numeri che salva a DB
    @PostMapping("/upload-csv")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a CSV file to upload.";
        } else {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                String[] values = null;
                List<DataEntry> entries = new ArrayList<>();
                while ((values = csvReader.readNext()) != null) {
                    DataEntry dataEntry = new DataEntry();
                    dataEntry.setId(values[0]);
                    dataEntry.setPhoneNumber(values[1]);
                    dataEntry.setDateLoad(Date.valueOf(LocalDate.now()));
                    entries.add(dataEntry);
                }
                dataEntryRepo.saveAll(entries);
                return "File successfully uploaded and data inserted into the database!";
            } catch (Exception e) {
                return "An error occurred while processing the CSV file.";
            }
        }
    }


    @GetMapping(value = "/controllAndSave", produces = "application/json")
    public String controllAndsavePhoneNumber()  throws SQLException {
        try {
            // Save the numbers to database
            boolean save = phoneNumberService.saveNumbers();
            if (save == true) {
                return "File uploaded successfully.";
            }else{
                return "Error or no file uploaded.";
            }

        } catch (Exception e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }
}

