package com.employeemanagement.utilities;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CSVUtils {

	private CSVUtils() {

	}

	public static Iterable<CSVRecord> readCSVFile(String filePath) throws IOException {
		FileReader employeeCSVReader = new FileReader(filePath);
		return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(employeeCSVReader);
	}

	public static void saveCSVFile(String filePath, Object value, String... headers) throws IOException {
		try (FileWriter writer = new FileWriter(filePath)) {
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));
			csvPrinter.printRecords(value);
			csvPrinter.close();
		}
	}
}
