package org.arabius.platform.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.Room;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvRoomLoader {

    public static List<Room> loadRooms(String filePath) throws IOException {
        List<Room> roomList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Room room = new Room();
                room.setId(Integer.parseInt(csvRecord.get("id")));
                room.setName(csvRecord.get("name"));
                room.setCapacity(Integer.parseInt(csvRecord.get("capacity")));
                room.setBranchId(Integer.parseInt(csvRecord.get("branch_id")));
                roomList.add(room);
            }
        } 

        return roomList;
    }
}