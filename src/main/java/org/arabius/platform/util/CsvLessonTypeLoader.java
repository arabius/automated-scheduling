package org.arabius.platform.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.GuideRequirment;
import org.arabius.platform.domain.LessonType;
import org.arabius.platform.domain.RoomRequirment;

public class CsvLessonTypeLoader {

    public static List<LessonType> loadLessonTypes(String filePath) throws IOException {
        List<LessonType> lessonTypeList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            new FakerGuideRequirments();
            ArrayList<GuideRequirment> $guideRequiments = FakerGuideRequirments.getGuideRequirments();

            new FakerRoomRequirments();
            ArrayList<RoomRequirment> $roomRequirments = FakerRoomRequirments.getRoomRequirments();
            
            if ($roomRequirments.size() < 3) {
                throw new RuntimeException("Room requirements list must have at least 3 elements");
            }

            for (CSVRecord csvRecord : csvParser) {
                LessonType lessonType = new LessonType();
                lessonType.setId(Integer.parseInt(csvRecord.get("id")));
                lessonType.setName(csvRecord.get("name_en"));
                lessonType.setEnforceRoomCapacity(Boolean.parseBoolean(csvRecord.get("enforce_room_capacity_limits")));
                GuideRequirment guideRequirment = $guideRequiments.stream()
                        .filter(gr -> gr.getKey().equals(csvRecord.get("guide_requirement")))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Guide requirement not found for lesson type"));
                lessonType.setGuideRequirment(guideRequirment);
                RoomRequirment roomRequirment = $roomRequirments.stream()
                        .filter(rr -> rr.getKey().equals(csvRecord.get("room_requirement")))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Room requirement not found for lesson type"));
                lessonType.setRoomRequirment(roomRequirment);
                lessonType.setAllowVideoCall(Boolean.parseBoolean(csvRecord.get("allow_video_call")));

                lessonTypeList.add(lessonType);
            }
        }

        return lessonTypeList;
    }
}