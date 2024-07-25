package org.arabius.platform.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.Lesson;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvLessonLoader {

    public static List<Lesson> loadLessons(String filePath) throws IOException {
        List<Lesson> lessonList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Lesson lesson = new Lesson();
                lesson.setId(Integer.parseInt(csvRecord.get("id")));
                lesson.setLevel(csvRecord.get("level"));
                lesson.setDate(LocalDate.parse(csvRecord.get("date")));
                lesson.setStart(LocalTime.parse(csvRecord.get("start")));
                lesson.setEnd(LocalTime.parse(csvRecord.get("end")));
                lesson.setStudentGroupHash(csvRecord.get("student_group_hash"));
                lesson.setLessonType(csvRecord.get("lesson_type"));
                lesson.setBranch(Integer.parseInt(csvRecord.get("branch_id")));
                lesson.setGuideName(csvRecord.get("guide_name"));
                lessonList.add(lesson);
            }
        }

        return lessonList;
    }
}