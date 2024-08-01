package org.arabius.platform.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.arabius.platform.domain.Guide;
import org.arabius.platform.domain.GuideSlot;
import org.arabius.platform.domain.Lesson;
import org.arabius.platform.domain.LessonType;
import org.arabius.platform.domain.Room;
import org.arabius.platform.domain.RoomPriority;
import org.arabius.platform.domain.Timeslot;
import org.arabius.platform.domain.Timetable;
import org.arabius.platform.util.CsvGuideLoader;
import org.arabius.platform.util.CsvGuideSlotsLoader;
import org.arabius.platform.util.CsvLessonLoader;
import org.arabius.platform.util.CsvLessonTypeLoader;
import org.arabius.platform.util.CsvRoomLoader;
import org.arabius.platform.util.CsvRoomPriorityLoader;


public class DemoDataResource {
    public static Timetable buildTimetable() {
                List<Room> rooms = new ArrayList<>();

        try {
            rooms = CsvRoomLoader.loadRooms("data/rooms.csv");
        } catch (IOException ex) {
       
        }

        List<Lesson> lessons = new ArrayList<>();
        try {
            lessons = CsvLessonLoader.loadLessons("data/lessons.csv");
        } catch (IOException e) {

        }
        
        List<Guide> guides = new ArrayList<>();
        try {
            guides = CsvGuideLoader.loadGuides("data/guides.csv");
        } catch (IOException e) {

        }      
        
        List<GuideSlot> guideSlots = new ArrayList<>();
        try {
            guideSlots = CsvGuideSlotsLoader.loadGuideSlots("data/guideslots.csv");
        } catch (IOException e) {

        }

        List<RoomPriority> roomPriorities = new ArrayList<>();
        try {
            roomPriorities = CsvRoomPriorityLoader.loadRoomPriorities("data/roompriorities.csv");
        } catch (IOException ex) {

        }

        List<LessonType> lessonTypes = new ArrayList<>();
        try {
            lessonTypes = CsvLessonTypeLoader.loadLessonTypes("data/lessontypes.csv");
        } catch (IOException ex) {

        }
       

       for (RoomPriority roomPriority : roomPriorities) {
            for (Room room : rooms) {
                if (roomPriority.getRoomId() == room.getId()) {
                    room.addRoomPriority(roomPriority);
                    break;
                }
            }
        }
        
        for (Guide guide : guides) {
            guide.addMatchingGuideslots(guideSlots);
        }

        List<Timeslot> timeSlots = new ArrayList<>();

        for (Lesson lesson : lessons) {
            LocalDate date = lesson.getDate();
            LocalTime startTime = lesson.getStart();
            LocalTime endTime = lesson.getEnd();
            Integer slotId = lesson.getSlotId();

            if (slotId != null) {
                Timeslot timeSlot = new Timeslot(slotId, date, startTime, endTime);
                if (!timeSlots.contains(timeSlot)) {
                    timeSlots.add(timeSlot);
                }
            }

            if (lesson.getInitialRoomId() != null) {
                for (Room room : rooms) {
                    if (Integer.valueOf(room.getId()).equals(lesson.getInitialRoomId())) {
                        lesson.setRoom(room);
                        break;
                    }
                }
            }

            if (lesson.getInitialGuideId() != null) {
                for (Guide guide : guides) {
                    if (Integer.valueOf(guide.getId()).equals(lesson.getInitialGuideId())) {
                        lesson.setGuide(guide);
                        break;
                    }
                }
            }

            for (LessonType lessonType : lessonTypes) {
                if (Integer.valueOf(lessonType.getId()).equals(lesson.getInitialLessonTypeId())) {
                    lesson.setLessonType(lessonType);
                    break;
                }
            }
        }
        
        // Now you can use the timeSlots list for further processing or manipulation

        lessons.removeIf(lesson -> "Admin Meeting".equals(lesson.getLevel()));

        return new Timetable("Demo_data", rooms, lessons, guides, timeSlots);
    }
}
