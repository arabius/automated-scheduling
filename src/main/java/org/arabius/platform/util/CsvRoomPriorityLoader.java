package org.arabius.platform.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.RoomPriority;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvRoomPriorityLoader {

	public static List<RoomPriority> loadRoomPriorities(String filePath) throws IOException {
		List<RoomPriority> roomPriorityList = new ArrayList<>();

		try (FileReader reader = new FileReader(filePath);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

			for (CSVRecord csvRecord : csvParser) {
				RoomPriority roomPriority = new RoomPriority();
				roomPriority.setRoomId(Integer.parseInt(csvRecord.get("room_id")));
				roomPriority.setPriority(Integer.parseInt(csvRecord.get("priority")));
				roomPriority.setLessonType(csvRecord.get("name_en"));
				roomPriorityList.add(roomPriority);
			}
		}

		return roomPriorityList;
	}
}