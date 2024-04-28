package controller;

import entity.DataEntry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import repository.DataEntryRepository;
import service.PhoneNumberService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;


@RestController
@RequestMapping("/api")
public class PhoneController {
    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private DataEntryRepository dataEntryRepo;

    //Chiamata post che prende in input un file csv con una lista di numeri che salva a DB
    @PostMapping("/upload_csv")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.ok("File not uploaded");
        } else {
           /* try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                String[] values = null;
                List<DataEntry> entries = new ArrayList<>();
                while ((values = csvReader.readNext()) != null) {
                    DataEntry dataEntry = new DataEntry();
                    dataEntry.setId(values[0]);
                    dataEntry.setPhoneNumber(values[1]);
                    dataEntry.setDateLoad(Date.valueOf(LocalDate.now()));
                    entries.add(dataEntry);
                }*/
            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] data = line.split(","); // Assumendo che il separatore sia una virgola
                    DataEntry record = new DataEntry();
                    record.setId(data[0]);
                    record.setPhoneNumber(data[1]);
                    record.setDateLoad(Date.valueOf(LocalDate.now()));
                    dataEntryRepo.save(record);
                }
                return ResponseEntity.ok("File uploaded successfully!");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
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

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Endpoint is working");
    }

}

