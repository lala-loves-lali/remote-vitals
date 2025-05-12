package com.remote_vitals.backend.services;

import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.vital.enums.VitalStatus;
import com.remote_vitals.backend.vital.entities.VitalRecord;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VitalReportTxtService {

    @Value("${path.download}")
    String downloadPath;

    // Static counter to generate unique file names
    private static int reportCounter = 1;

    public String downloadVitalReportTxt(VitalReport vitalReport) {
        String fileName = "vitalReport" + reportCounter++ + ".txt";
        File file = new File(downloadPath, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Get patient name
            Patient patient = vitalReport.getPatient();
            String fullName = patient.getFirstName() + " " + patient.getLastName();
            writer.write("Patient Name: " + fullName);
            writer.newLine();

            // Format and write submission time
            String formattedTime = vitalReport.getReportWhenMade()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
            writer.write("TimeWhenSubmitted : " + formattedTime);
            writer.newLine();

            // Write abnormal vital records
            List<VitalRecord> records = vitalReport.getVitalRecords();
            for(VitalRecord record : records){
                    writer.write(
                            "\t" + record.getClass().getSimpleName() + " : " + record.getValue()
                                    + (
                                    record.getStatus() == VitalStatus.ABNORMAL ? " (Abnormal)" : ""
                            )
                    );
                    writer.newLine();
            }
        } catch (IOException e) {
            return "Error";
        }
        return fileName;
    }
}
