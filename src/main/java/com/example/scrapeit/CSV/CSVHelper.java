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
import org.apache.commons.csv.QuoteMode;

public class CSVHelper {

    public static ByteArrayInputStream licencesToCSV(List<License> licenses) {

        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

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
                        license.getExpirationDate(),
                        license.getIssueDate(),
                        license.getBoardAction()
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new CSVFileImportException("Couldn't import data to CSV file: " + e.getMessage());
        }
    }
}
