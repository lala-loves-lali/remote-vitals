package com.remote_vitals.backend.reportGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.vital.entities.*;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;

import com.remote_vitals.backend.user.enums.Gender;

public class ReportGenerator {

    private Doctor doctor;
    private Patient patient;
    private CheckUp checkup;
    private java.util.List<VitalReport> vitalReports;
    
    // Maps to store vital records by type
    private Map<String, java.util.List<VitalRecord>> vitalRecordsByType;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReportGenerator(Doctor doctor, Patient patient, CheckUp checkup, java.util.List<VitalReport> vitalReports) {
        this.doctor = doctor;
        this.patient = patient;
        this.checkup = checkup;
        this.vitalReports = vitalReports;
        this.vitalRecordsByType = new HashMap<>();
        
        // Initialize and categorize vital records
        categorizeVitalRecords();
    }
    
    /**
     * Categorizes vital records by their type
     */
    private void categorizeVitalRecords() {
        // Process all vital reports and collect records by type
        for (VitalReport report : vitalReports) {
            if (report.getVitalRecords() != null) {
                for (VitalRecord record : report.getVitalRecords()) {
                    String recordType = getVitalRecordType(record);
                    
                    // Add to appropriate list
                    if (!vitalRecordsByType.containsKey(recordType)) {
                        vitalRecordsByType.put(recordType, new ArrayList<>());
                    }
                    vitalRecordsByType.get(recordType).add(record);
                }
            }
        }
        
        // Sort each list by date (using VitalReport's report date)
        for (java.util.List<VitalRecord> records : vitalRecordsByType.values()) {
            records.sort(Comparator.comparing(record -> record.getVitalReport().getReportWhenMade()));
        }
    }
    
    /**
     * Returns the type name of a vital record
     */
    private String getVitalRecordType(VitalRecord record) {
        if (record instanceof BloodPressureSystolic) return "Blood Pressure Systolic";
        if (record instanceof BloodPressureDiastolic) return "Blood Pressure Diastolic";
        if (record instanceof BodyTemperature) return "Body Temperature";
        if (record instanceof HeartRate) return "Heart Rate";
        if (record instanceof RespiratoryRate) return "Respiratory Rate";
        if (record instanceof Weight) return "Weight";
        if (record instanceof Height) return "Height";
        if (record instanceof Haemoglobin) return "Haemoglobin";
        if (record instanceof RBC) return "Red Blood Cells";
        if (record instanceof WBC) return "White Blood Cells";
        if (record instanceof PlateletCount) return "Platelet Count";
        if (record instanceof BloodVolume) return "Blood Volume";
        return "Unknown Vital Type";
    }

    public void createReport() {
        // Use a more reliable path with timestamp to avoid conflicts
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = patient.getFirstName() + "_" + patient.getLastName() + "_report_" + timestamp + ".pdf";
        String dest = System.getProperty("user.dir") + File.separator + fileName;
        
        System.out.println("Generating PDF report at: " + dest);
        FileOutputStream fos = null;

        try {
            // Create file output stream
            File file = new File(dest);
            fos = new FileOutputStream(file);
            
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();

            System.out.println("Creating PDF document structure...");
            
            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Patient Medical History Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Info Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);
            table.setSpacingAfter(10f);
            float[] columnWidths = {1f, 4f};
            table.setWidths(columnWidths);

            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 11);

            // Add patient and doctor info
            addTableRow(table, "Patient Name:", this.patient.getFirstName() + " " + this.patient.getLastName(), headFont, dataFont);
            addTableRow(table, "Patient ID:", String.valueOf(this.patient.getId()), headFont, dataFont);
            addTableRow(table, "Doctor Name:", this.doctor.getFirstName() + " " + this.doctor.getLastName(), headFont, dataFont);
            addTableRow(table, "Doctor ID:", String.valueOf(this.doctor.getId()), headFont, dataFont);
            addTableRow(table, "Report Date:", this.checkup.getTimeWhenMade().format(dateFormatter), headFont, dataFont);

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Add checkup details
            Paragraph prescriptionTitle = new Paragraph("Prescription:", headFont);
            Paragraph prescriptionText = new Paragraph(this.checkup.getPrescription(), dataFont);
            prescriptionTitle.setAlignment(Element.ALIGN_LEFT);
            prescriptionText.setAlignment(Element.ALIGN_LEFT);      
            document.add(prescriptionTitle);
            document.add(prescriptionText);
            document.add(Chunk.NEWLINE);

            // Add feedback
            Paragraph feedbackTitle = new Paragraph("Feedback:", headFont);
            Paragraph feedbackText = new Paragraph(this.checkup.getFeedback(), dataFont);
            feedbackTitle.setAlignment(Element.ALIGN_LEFT);
            feedbackText.setAlignment(Element.ALIGN_LEFT);
            document.add(feedbackTitle);
            document.add(feedbackText);

            System.out.println("Adding vital records data to PDF...");

            // Add sections for each vital type
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            
            for (Map.Entry<String, java.util.List<VitalRecord>> entry : vitalRecordsByType.entrySet()) {
                String vitalType = entry.getKey();
                java.util.List<VitalRecord> records = entry.getValue();
                
                System.out.println("Processing " + vitalType + " records (" + records.size() + " entries)");
                
                // Only process if we have records
                if (records.size() > 0) {
                    // Add section title
                    Paragraph sectionTitle = new Paragraph(vitalType + " Records", sectionFont);
                    sectionTitle.setSpacingBefore(10f);
                    document.add(sectionTitle);
                    
                    // Add records table
                    PdfPTable recordsTable = new PdfPTable(3);
                    recordsTable.setWidthPercentage(100);
                    recordsTable.setSpacingBefore(5f);
                    recordsTable.setSpacingAfter(10f);
                    
                    // Add headers
                    PdfPCell dateHeader = new PdfPCell(new Phrase("Date", headFont));
                    dateHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    recordsTable.addCell(dateHeader);
                    
                    PdfPCell valueHeader = new PdfPCell(new Phrase("Value", headFont));
                    valueHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    recordsTable.addCell(valueHeader);
                    
                    PdfPCell noteHeader = new PdfPCell(new Phrase("Notes", headFont));
                    noteHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    recordsTable.addCell(noteHeader);
                    
                    // Add data rows
                    for (VitalRecord record : records) {
                        recordsTable.addCell(new Phrase(
                            record.getVitalReport().getReportWhenMade().format(dateFormatter), 
                            dataFont
                        ));
                        recordsTable.addCell(new Phrase(
                            String.valueOf(record.getValue()), 
                            dataFont
                        ));
                        recordsTable.addCell(new Phrase("", dataFont)); // Empty notes for now
                    }
                    
                    document.add(recordsTable);
                    
                    // Generate graph if we have enough data points (more than 3)
                    if (records.size() > 3) {
                        System.out.println("Generating graph for " + vitalType);
                        
                        // Extract values and dates for the graph
                        float[] values = new float[records.size()];
                        String[] dates = new String[records.size()];
                        
                        for (int i = 0; i < records.size(); i++) {
                            values[i] = records.get(i).getValue();
                            dates[i] = records.get(i).getVitalReport().getReportWhenMade()
                                .format(DateTimeFormatter.ofPattern("MM-dd"));
                        }
                        
                        // Create single vital type graph
                        BufferedImage graphImage = createSingleVitalGraph(values, dates, vitalType);
                        
                        if (graphImage != null) {
                            System.out.println("Adding graph image to PDF");
                            Image img = ImageUtil.bufferedImageToPdfImage(graphImage);
                            img.scaleToFit(500, 250);
                            img.setAlignment(Element.ALIGN_CENTER);
                            document.add(img);
                        } else {
                            System.out.println("Failed to create graph image");
                        }
                    }
                    
                    document.add(Chunk.NEWLINE);
                }
            }

            // Add footer
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph footer = new Paragraph("Generated by Remote Vitals System on " + 
                                            new Date().toString(), footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            // Close document
            document.close();
            writer.close();
            System.out.println("PDF created successfully at: " + dest);
            
            // Try to open the PDF
            try {
                File pdfFile = new File(dest);
                if (pdfFile.exists()) {
                    System.out.println("PDF file exists, size: " + pdfFile.length() + " bytes");
                    
                    // Print absolute path for debugging
                    System.out.println("Absolute path: " + pdfFile.getAbsolutePath());
                } else {
                    System.out.println("Error: PDF file does not exist after creation");
                }
            } catch (Exception e) {
                System.out.println("Error checking PDF file: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close the FileOutputStream
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    System.err.println("Error closing file stream: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Helper method to add a row to a table
     */
    private void addTableRow(PdfPTable table, String header, String data, Font headerFont, Font dataFont) {
        PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(headerCell);

        PdfPCell dataCell = new PdfPCell(new Phrase(data, dataFont));
        dataCell.setPaddingLeft(5);
        table.addCell(dataCell);
    }
    
    /**
     * Creates a graph for a single vital sign type
     */
    private BufferedImage createSingleVitalGraph(float[] values, String[] dates, String vitalType) {
        try {
            // Convert float array to int array (required by the graph exporter)
            int[] intValues = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                intValues[i] = Math.round(values[i]);
            }
            
            // Create a clean, focused graph without extra text or labels
            SingleVitalGraphExporter exporter = new SingleVitalGraphExporter(
                intValues, 
                dates,
                vitalType
            );
            
            return exporter.createPicture();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Main method to test the report generation functionality
     */
    public static void main(String[] args) {
        try {
            System.out.println("Testing ReportGenerator...");
            
            // Create sample doctor
            Doctor doctor = new Doctor();
            doctor.setId(1);
            doctor.setFirstName("John");
            doctor.setLastName("Smith");
            doctor.setGender(Gender.MALE);
            doctor.setEmail("john.smith@hospital.com");
            doctor.setPhoneNumber("123-456-7890");
            doctor.setQualificationString("MD, Cardiology Specialist");
            
            // Create sample patient
            Patient patient = new Patient();
            patient.setId(2);
            patient.setFirstName("Emily");
            patient.setLastName("Jones");
            patient.setGender(Gender.FEMALE);
            patient.setEmail("emily.jones@example.com");
            patient.setPhoneNumber("987-654-3210");
            
            // Create sample checkup
            CheckUp checkup = new CheckUp();
            checkup.setId(3);
            checkup.setTimeWhenMade(LocalDateTime.now());
            checkup.setPrescription("Regular health checkup");
            checkup.setFeedback("Patient is in good health");
            checkup.setDoctor(doctor);
            checkup.setPatient(patient);
            
            // Create sample vital reports with different dates
            java.util.List<VitalReport> vitalReports = new ArrayList<>();
            
            System.out.println("Creating sample vital reports and records...");
            
            // Create reports spanning 5 days - one report per day
            for (int day = 0; day < 5; day++) {
                LocalDateTime reportDate = LocalDateTime.now().minusDays(day);
                
                // Create a vital report for this day
                VitalReport report = new VitalReport();
                report.setId(100 + day);
                report.setReportWhenMade(reportDate);
                report.setPatient(patient);
                
                // Create vital records for this report
                java.util.List<VitalRecord> records = new ArrayList<>();
                
                try {
                    // Add heart rate record
                    HeartRate heartRate = new HeartRate();
                    heartRate.setId(1000 + day);
                    heartRate.setValue(72 + day); // slightly different values each day
                    records.add(heartRate);
                    
                    // Add body temperature record
                    BodyTemperature temperature = new BodyTemperature();
                    temperature.setId(2000 + day);
                    temperature.setValue(36.5f + (day * 0.1f)); // slightly different values
                    records.add(temperature);
                    
                    // Add blood pressure systolic record
                    BloodPressureSystolic systolic = new BloodPressureSystolic();
                    systolic.setId(3000 + day);
                    systolic.setValue(120 + (day * 2)); // slightly different values
                    records.add(systolic);
                    
                    // Add blood pressure diastolic record
                    BloodPressureDiastolic diastolic = new BloodPressureDiastolic();
                    diastolic.setId(4000 + day);
                    diastolic.setValue(80 + day); // slightly different values
                    records.add(diastolic);
                    
                    // Set relationships properly - first set report to each record
                    for (VitalRecord record : records) {
                        record.setVitalReport(report);
                    }
                    
                    // Then set records to report
                    report.setVitalRecords(records);
                    
                    // Add report to list
                    vitalReports.add(report);
                    
                    System.out.println("Created report for day " + day + " with " + records.size() + " vital records");
                    
                } catch (Exception e) {
                    System.err.println("Error creating vital records for day " + day + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Print summary before report generation
            System.out.println("\nSummary before report generation:");
            System.out.println("Doctor: " + doctor.getFirstName() + " " + doctor.getLastName());
            System.out.println("Patient: " + patient.getFirstName() + " " + patient.getLastName());
            System.out.println("Number of vital reports: " + vitalReports.size());
            
            // Check that reports have vital records
            for (int i = 0; i < vitalReports.size(); i++) {
                VitalReport report = vitalReports.get(i);
                System.out.println("Report " + i + " has " + 
                    (report.getVitalRecords() != null ? report.getVitalRecords().size() : "null") + 
                    " vital records, date: " + report.getReportWhenMade());
            }
            
            System.out.println("\nGenerating report...");
            
            // Create the report generator and generate the report
            ReportGenerator generator = new ReportGenerator(doctor, patient, checkup, vitalReports);
            generator.createReport();
            
            System.out.println("\nReport generation test complete!");
            
        } catch (Exception e) {
            System.err.println("Error testing ReportGenerator: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


