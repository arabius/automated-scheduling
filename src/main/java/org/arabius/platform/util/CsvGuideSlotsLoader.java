package org.arabius.platform.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.GuideSlot;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvGuideSlotsLoader {

    public static List<GuideSlot> loadGuideSlots(String filePath) throws IOException {
        List<GuideSlot> guideSlots = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                GuideSlot guideSlot = new GuideSlot();
                guideSlot.setDate(LocalDate.parse(csvRecord.get("date")));
                guideSlot.setGuideId(Integer.parseInt(csvRecord.get("guide_id")));
                guideSlot.setSlotIds(csvRecord.get("slot_ids"));
                guideSlot.setStartTime(LocalTime.parse(csvRecord.get("start_time")));
                guideSlot.setEndTime(LocalTime.parse(csvRecord.get("end_time")));
                guideSlots.add(guideSlot);
            }
        }

        return guideSlots;
    }
}