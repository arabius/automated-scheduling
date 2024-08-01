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
import org.arabius.platform.rest.TimetableDemoResource.DemoData;
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

            new DemoDataResource();
            Timetable unsolvedTimetable = DemoDataResource.buildTimetable();
            //Timetable solvedTimetable = solver.solve(unsolvedTimetable);

            PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark(unsolvedTimetable);
        benchmark.benchmarkAndShowReportInBrowser();

        return "done";
    }
    
}
