package org.arabius.platform.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.Timeslot;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CsvTimeslotLoader {

	public static List<Timeslot> loadTimeslots(String filePath) throws IOException {
		List<Timeslot> timeslotList = new ArrayList<>();

		try (FileReader reader = new FileReader(filePath);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
            int i = 0;

			for (CSVRecord csvRecord : csvParser) {
				Timeslot timeslot = new Timeslot();
				timeslot.setId(i++);
                timeslot.setDayOfWeek(LocalDate.parse(csvRecord.get("date")));
				timeslot.setStartTime(LocalTime.parse(csvRecord.get("start")));
				timeslot.setEndTime(LocalTime.parse(csvRecord.get("end")));
				timeslotList.add(timeslot);
			}
		}

		return timeslotList;
	}
}