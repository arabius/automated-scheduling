package org.arabius.platform.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.Lesson;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvLessonLoader {

    public static List<Lesson> loadLessons(String filePath) throws IOException {
        List<Lesson> lessonList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (FileReader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Lesson lesson = new Lesson();
                lesson.setId(Integer.parseInt(csvRecord.get("id")));
                lesson.setLevel(Integer.parseInt(csvRecord.get("level")));
                lesson.setDate(LocalDate.parse(csvRecord.get("date")));
                lesson.setStart(LocalTime.parse(csvRecord.get("start")));
                lesson.setEnd(LocalTime.parse(csvRecord.get("end")));
                lesson.setBufferStart(LocalDateTime.parse(csvRecord.get("buffer_start"), formatter));
                lesson.setBufferEnd(LocalDateTime.parse(csvRecord.get("buffer_end"), formatter));
                lesson.setStudentGroupHash(csvRecord.get("student_group_hash"));
                //lesson.setInitialLessonTypeId(Integer.parseInt(csvRecord.get("lesson_type_id")));
                lesson.setBranchId(Integer.parseInt(csvRecord.get("branch_id")));
                lesson.setSlotId(parseInteger(csvRecord.get("slot_id")));
                lesson.setInitialGuideId(parseInteger(csvRecord.get("guide_id")));
                lesson.setInitialRoomId(parseInteger(csvRecord.get("room_id")));
                lessonList.add(lesson);
            }
        }

        return lessonList;
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) {
            return null; // or you can return a default value like 0
        }
        return Integer.parseInt(value);
    }
}