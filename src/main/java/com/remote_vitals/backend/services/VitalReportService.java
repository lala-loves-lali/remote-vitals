package com.remote_vitals.backend.services;

import com.remote_vitals.backend.db_handler.StaticDataClass;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.repositories.UserRepository;
import com.remote_vitals.backend.vital.entities.*;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.file.Paths;
import java.nio.file.Path;

@Service
public class VitalReportService {
    // Attributes and Beans
    @Value("${path.csv-files-directory}")
    private String csvFilesDirectory;

    private final UserRepository userRepository;
    private final CsvService csvService;
    private final DateAndTimeService dateAndTimeService;

    // Constructor
    public VitalReportService(
            UserRepository userRepository,
            CsvService csvService,
            DateAndTimeService dateAndTimeService
    ) {
        this.userRepository = userRepository;
        this.csvService = csvService;
        this.dateAndTimeService = dateAndTimeService;
    }
    // Methods
    @Transactional
    public String readCsvForCurrentUser() {
        if (StaticDataClass.currentUserId == null) {
            throw new RuntimeException("Current User ID is not present");
        }

        try {
            // Normalize the file path
            String filePath = Paths.get(csvFilesDirectory, StaticDataClass.currentUserId + ".csv").toString();
            System.out.println("CSV files directory from configuration: " + csvFilesDirectory);
            System.out.println("Resolved file path: " + filePath);

            // Parse the CSV file
            List<Map<String, String>> reports = csvService.parseCsv(filePath);
            System.out.println("Parsed data: " + reports);

            if (reports.isEmpty()) {
                System.out.println("No valid data found in the file.");
                return "File Format Invalid";
            }

            List<VitalReport> vitalReports = new ArrayList<>();
            reports.forEach(report -> {
                System.out.println("Processing report: " + report);
                VitalReport vitalReportCreated = createVitalReport(report);

                if (vitalReportCreated == null) {
                    throw new RuntimeException("Invalid File Format for report: " + report);
                }

                vitalReports.add(vitalReportCreated);
            });

            // Connecting saved reports to the user
            userRepository.findById(StaticDataClass.currentUserId).ifPresent(user -> {
                if (user instanceof Patient) {
                    Patient patient = (Patient) user;
                    patient.getVitalReports().clear();
                    vitalReports.forEach(vitalReport -> {
                        vitalReport.setPatient(patient);
                        patient.getVitalReports().add(vitalReport);
                    });
                    System.out.println("Vital reports updated for patient: " + patient.getId());
                }
            });

            return "Csv Parsed Successfully";
        } catch (IOException ex) {
            System.out.println("IOException during file processing: " + ex.getMessage());
            return "File Not Found Or Invalid Format";
        } catch (RuntimeException ex) {
            System.out.println("RuntimeException during file processing: " + ex.getMessage());
            return "File Format Invalid";
        }
    }

    public VitalReport createVitalReport(Map<String,String> vitalRecordList){
        if(vitalRecordList == null || vitalRecordList.isEmpty()) return null;
        VitalReport vitalReport = new VitalReport();
        vitalReport.setVitalRecords(new ArrayList<VitalRecord>());
        // finds the report time of the report list
        for(Map.Entry<String,String> entry : vitalRecordList.entrySet()){
            if(entry.getKey().equals("date-time")) {
                if(dateAndTimeService.matchDateformat(entry.getValue(), "yyyy-MM-dd'T'HH:mm:ss")){
                    LocalDateTime timeWhenMade = dateAndTimeService.analyzeDateTimeFormat(entry.getValue(),"yyyy-MM-dd'T'HH:mm:ss");
                    if(timeWhenMade == null)
                        throw new IllegalArgumentException("Date Format Is not Valid");
                    vitalReport.setReportWhenMade(timeWhenMade);
                    break;
                }else{
                    throw new IllegalArgumentException("Date Format Is not Valid");
                }
            }
        }
        // removing the date-time key value
        vitalRecordList.remove("date-time");
        // make the vital record objects for the vitalReport
        try {
            vitalRecordList.forEach((k,v)->{
                VitalRecord vitalRecord = null;
                    switch (k) {
                        case "body-temperature" ->
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new BodyTemperature(Float.parseFloat(v)));
                        case "heart-rate" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new HeartRate(Float.parseFloat(v)));
                        }
                        case "rbc" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new RBC(Float.parseFloat(v)));
                        }
                        case "wbc" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new WBC(Float.parseFloat(v)));
                        }
                        case "platelet-count" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new PlateletCount(Float.parseFloat(v)));
                        }
                        case "respiratory-rate" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new RespiratoryRate(Float.parseFloat(v)));
                        }
                        case "blood-pressure-systolic" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new BloodPressureSystolic(Float.parseFloat(v)));
                        }
                        case "blood-pressure-diastolic" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new BloodPressureDiastolic(Float.parseFloat(v)));
                        }
                        case "blood-volume" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new BloodVolume(Float.parseFloat(v)));
                        }
                        case "haemoglobin" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new Haemoglobin(Float.parseFloat(v)));
                        }
                        case "height" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new Height(Float.parseFloat(v)));
                        }
                        case "weight" -> {
                            vitalReport.getVitalRecords()
                                    .add(vitalRecord = new Weight(Float.parseFloat(v)));
                        }
                        default -> {
                            System.out.println("Invalid Key In The Report Map");
                            throw new IllegalArgumentException("Invalid Key In The Report Map");
                        }
                    }
                    vitalRecord.setVitalReport(vitalReport);
            });
            System.out.println("==========================");
            System.out.println(vitalReport.getVitalRecords());
            return vitalReport;
        } catch (Exception ex) {
            return null;
        }
    }
}
