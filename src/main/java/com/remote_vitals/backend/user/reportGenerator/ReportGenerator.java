package com.remote_vitals.backend.user.reportGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.Date;

import java.awt.image.BufferedImage;

public class ReportGenerator {

    private String patientName;
    private String doctorName;
    private String patientId;
    private String doctorId;
    private String chiefCompliant;

    private int[][] dataSeries;
    private String[] seriesNames;


    public ReportGenerator(String patientName, String doctorName, String patientId, String doctorId, String chiefCompliant, int[][] dataSeries, String[] seriesNames) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.chiefCompliant = chiefCompliant;

        this.dataSeries = dataSeries;
        this.seriesNames = seriesNames;
    }

    public void createReport() {
        String dest = "medical_history_report.pdf";

        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD | Font.ITALIC | Font.UNDERLINE);
            Paragraph title = new Paragraph("Patient Medical History Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Info Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(30f);
            float[] columnWidths = {10f, 40f};
            table.setWidths(columnWidths);

            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 11);

            String[][] headerTableCells = {
                    {"Patient Name: ", this.patientName},
                    {"Patient ID:", this.patientId},
                    {"Doctor Name: ", this.doctorName},
                    {"Doctor Id: ", this.doctorId},
                    {"Chief Complaint: ", this.chiefCompliant}
            };

            for (String[] row : headerTableCells) {
                PdfPCell header = new PdfPCell(new Phrase(row[0], headFont));
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(header);

                PdfPCell cell = new PdfPCell(new Phrase(row[1], dataFont));
                cell.setPaddingLeft(5);
                table.addCell(cell);
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Timestamp
            Font timestampFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph timestamp = new Paragraph("Generated on: " + new Date(), timestampFont);
            timestamp.setAlignment(Element.ALIGN_RIGHT);
            document.add(timestamp);

            // Add Graph
            ArrayGraphExporter exporter = new ArrayGraphExporter(dataSeries, seriesNames);
            BufferedImage graphImage = exporter.createPicture();
            if (graphImage != null) {
                Image img = ImageUtil.bufferedImageToPdfImage(graphImage);
                img.scaleToFit(500, 300);
                img.setAlignment(Element.ALIGN_CENTER);
                document.add(img);
            }

            document.close();
            System.out.println("PDF created successfully at: " + dest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


