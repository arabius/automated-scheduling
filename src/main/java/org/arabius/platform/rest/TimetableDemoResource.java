package org.arabius.platform.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
        
        new DemoDataResource();
        return Response.ok(DemoDataResource.buildTimetable()).build();
    }

}
