package org.arabius.platform.rest;
import jakarta.ws.rs.Produces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.arabius.platform.domain.Guide;
import org.arabius.platform.domain.GuideSlot;
import org.arabius.platform.domain.Lesson;
import org.arabius.platform.domain.Room;
import org.arabius.platform.domain.RoomPriority;
import org.arabius.platform.domain.Timetable;
import org.arabius.platform.util.CsvGuideLoader;
import org.arabius.platform.util.CsvGuideSlotsLoader;
import org.arabius.platform.util.CsvLessonLoader;
import org.arabius.platform.util.CsvRoomLoader;
import org.arabius.platform.util.CsvRoomPriorityLoader;

import ai.timefold.solver.benchmark.api.PlannerBenchmark;
import ai.timefold.solver.benchmark.api.PlannerBenchmarkFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;


@Path("test")
public class TestResource {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String TestResource() {
            PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("benchmarkConfig.xml");
            //Solver<Timetable> solver = solverFactory.buildSolver();

            Timetable unsolvedTimetable = createTimetable();
            //Timetable solvedTimetable = solver.solve(unsolvedTimetable);

            PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark(unsolvedTimetable);
        benchmark.benchmarkAndShowReportInBrowser();

        return "done";
    }

    private Timetable createTimetable() {
                List<Room> rooms = new ArrayList<>();

        try {
            rooms = CsvRoomLoader.loadRooms("data/rooms.csv");
        } catch (IOException ex) {
            return new Timetable();
        }

        List<Lesson> lessons = new ArrayList<>();
        try {
            lessons = CsvLessonLoader.loadLessons("data/lessons.csv");
        } catch (IOException e) {
            return new Timetable();
        }
        
        List<Guide> guides = new ArrayList<>();
        try {
            guides = CsvGuideLoader.loadGuides("data/guides.csv");
        } catch (IOException ex) {
            return new Timetable();
        }      
        
        List<RoomPriority> roomPriorities = new ArrayList<>();
        try {
            roomPriorities = CsvRoomPriorityLoader.loadRoomPriorities("data/roompriorities.csv");
        } catch (IOException ex) {
            return new Timetable();
        }
       

       for (RoomPriority roomPriority : roomPriorities) {
            for (Room room : rooms) {
                if (roomPriority.getRoomId() == room.getId()) {
                    room.addRoomPriority(roomPriority);
                    break;
                }
            }
        }

        List<GuideSlot> guideSlots = new ArrayList<>();
        try {
            guideSlots = CsvGuideSlotsLoader.loadGuideSlots("data/guideslots.csv");
        } catch (IOException ex) {
            return new Timetable();
        }
        
        for (Guide guide : guides) {
            guide.addMatchingGuideslots(guideSlots);
        }
        lessons.removeIf(lesson -> "Admin Meeting".equals(lesson.getLevel()));

        return new Timetable("Test", rooms, lessons, guides);
    }

    
}
