package com.thecookiezen.business.boundary;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Path("/mapping/csv")
@Consumes(MediaType.APPLICATION_JSON)
public class MappingStorageResource {

    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final Logger log = Logger.getLogger(MappingStorageResource.class.getName());

    @POST
    public void toCsv(Map<String, String> map) {
        log.info(map);

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {
            fileWriter = new FileWriter("/opt/deploy/mapping_" + System.currentTimeMillis());
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                csvFilePrinter.printRecord(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        } catch (Exception e) {
            log.error("Error in CsvFileWriter.", e);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                log.error("Error while flushing/closing fileWriter/csvPrinter.", e);
            }
            log.info("CSV file was created successfully.");
        }
    }
}
