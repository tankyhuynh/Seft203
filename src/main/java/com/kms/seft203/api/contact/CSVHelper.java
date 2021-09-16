package com.kms.seft203.api.contact;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kms.seft203.domain.contact.Contact;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;


public class CSVHelper {
  public static String TYPE = "text/csv";

  public static boolean hasCSVFormat(MultipartFile file) {
    if (TYPE.equals(file.getContentType())
    		|| file.getContentType().equals("application/vnd.ms-excel")) {
      return true;
    }

    return false;
  }

  public static List<Contact> csvToEntity(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<Contact> developerTutorialList = new ArrayList<>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
        Contact developerTutorial = new Contact(
              csvRecord.get("id"),
              csvRecord.get("firstName"),
              csvRecord.get("lastName"),
              csvRecord.get("title"),
              csvRecord.get("department"),
              csvRecord.get("project"),
              csvRecord.get("avatar"),
              Integer.parseInt(csvRecord.get("employeeId"))
            );

    	  developerTutorialList.add(developerTutorial);
      }

      return developerTutorialList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream entityToCSV(List<Contact> contactList) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
      CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      for (Contact contact : contactList) {
        List<String> data = Arrays.asList(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getTitle(),
                contact.getDepartment(),
                contact.getProject(),
                contact.getAvatar(),
                String.valueOf(contact.getEmployeeId())
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }
}