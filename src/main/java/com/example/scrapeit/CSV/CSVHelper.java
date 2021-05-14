package com.example.scrapeit.CSV;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import com.example.scrapeit.exception.CSVFileImportException;
import com.example.scrapeit.model.License;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVHelper {

    public static ByteArrayInputStream licencesToCSV(List<License> licenses) {

        final CSVFormat format = CSVFormat.DEFAULT.withHeader("licenseNumber", "lastName", "firstName", "middleName", "city", "state", "status", "issueDate", "expirationDate", "boardAction");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), format)) {
            for (License license : licenses) {
                List<String> data = Arrays.asList(
                        license.getLicenseNumber(),
                        license.getLastName(),
                        license.getFirstName(),
                        license.getMiddleName(),
                        license.getCity(),
                        license.getState(),
                        license.getStatus(),
                        license.getIssueDate(),
                        license.getExpirationDate(),
                        license.getBoardAction()
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new CSVFileImportException("Couldn't import data to CSV file.");
        }
    }
}
