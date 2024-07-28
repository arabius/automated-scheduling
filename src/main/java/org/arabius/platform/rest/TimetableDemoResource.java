package org.arabius.platform.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.arabius.platform.domain.Guide;
import org.arabius.platform.domain.Lesson;
import org.arabius.platform.domain.Room;
import org.arabius.platform.domain.RoomPriority;
import org.arabius.platform.domain.Timeslot;
import org.arabius.platform.domain.Timetable;
import org.arabius.platform.util.CsvGuideLoader;
import org.arabius.platform.util.CsvLessonLoader;
import org.arabius.platform.util.CsvRoomLoader;
import org.arabius.platform.util.CsvRoomPriorityLoader;
import org.arabius.platform.util.CsvTimeslotLoader;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Demo data", description = "Timefold-provided demo school timetable data.")
@Path("demo-data")
public class TimetableDemoResource {

    public enum DemoData {
        SMALL,
        LARGE
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of demo data represented as IDs.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = DemoData.class, type = SchemaType.ARRAY))) })
    @Operation(summary = "List demo data.")
    @GET
    public DemoData[] list() {
        return DemoData.values();
    }

    
    
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Unsolved demo timetable.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Timetable.class)))})
    @Operation(summary = "Find an unsolved demo timetable by ID.")
    @GET
    @Path("/{demoDataId}")
    public Response generate(@Parameter(description = "Unique identifier of the demo data.",
            required = true) @PathParam("demoDataId") DemoData demoData) {

        List<Room> rooms = new ArrayList<>();

        try {
            rooms = CsvRoomLoader.loadRooms("data/rooms.csv");
        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading rooms from CSV file: " + ex.getMessage())
                    .build();
        }

        List<Lesson> lessons = new ArrayList<>();
        try {
            lessons = CsvLessonLoader.loadLessons("data/lessons.csv");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading lessons from CSV file: " + e.getMessage())
                    .build();
        }
        
        List<Guide> guides = new ArrayList<>();
        try {
            guides = CsvGuideLoader.loadGuides("data/guides.csv");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading guides from CSV file: " + e.getMessage())
                    .build();
        }      
        
        List<Timeslot> timeslots = new ArrayList<>();
        try {
            timeslots = CsvTimeslotLoader.loadTimeslots("data/timeslots.csv");
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading timeslots from CSV file: " + e.getMessage())
                    .build();
        }

        List<RoomPriority> roomPriorities = new ArrayList<>();
        try {
            roomPriorities = CsvRoomPriorityLoader.loadRoomPriorities("data/roompriorities.csv");
        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading room priorities from CSV file: " + ex.getMessage())
                    .build();
        }
       

       for (RoomPriority roomPriority : roomPriorities) {
            for (Room room : rooms) {
                if (roomPriority.getRoomId() == room.getId()) {
                    room.addRoomPriority(roomPriority);
                    break;
                }
            }
        }
        
        for (Lesson lesson : lessons) {
            for (Timeslot timeslot : timeslots) {
                if (lesson.getDate().equals(timeslot.getDayOfWeek()) &&
                    lesson.getBufferStart().toLocalTime().equals(timeslot.getStartTime()) &&
                    lesson.getBufferEnd().toLocalTime().equals(timeslot.getEndTime())) 
                {
                    lesson.setTimeslot(timeslot);
                    break; // No need to check the remaining timeslots for this lesson
                }
            }
        }

        lessons.removeIf(lesson -> "Admin Meeting".equals(lesson.getLevel()));
        
        return Response.ok(new Timetable(demoData.name(), timeslots, rooms, lessons, guides)).build();
    }

}
