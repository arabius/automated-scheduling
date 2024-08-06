package org.arabius.platform.domain;

import java.util.List;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;
import ai.timefold.solver.core.api.solver.SolverStatus;

@PlanningSolution
public class Timetable {


    private String name;

    private List<Timeslot> timeslots;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Room> rooms;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Guide> guides;

    @PlanningEntityCollectionProperty
    private List<Lesson> lessons;

    @PlanningScore
    private HardSoftBigDecimalScore score;

    // Ignored by Timefold, used by the UI to display solve or stop solving button
    private SolverStatus solverStatus;

    // No-arg constructor required for Timefold
    public Timetable() {
    }

    public Timetable(String name, HardSoftBigDecimalScore score, SolverStatus solverStatus) {
        this.name = name;
        this.score = score;
        this.solverStatus = solverStatus;
    }

    public Timetable(String name, List<Room> rooms, List<Lesson> lessons, List<Guide> guides) {
        this.name = name;
        this.rooms = rooms;
        this.lessons = lessons;
        this.guides = guides;
    }

    public Timetable(String name, List<Room> rooms, List<Lesson> lessons, List<Guide> guides, List<Timeslot> timeslots) {
        this.name = name;
        this.rooms = rooms;
        this.lessons = lessons;
        this.guides = guides;
        this.timeslots = timeslots;
    }


    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public String getName() {
        return name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Guide> getGuides() {
        return guides;
    }

    public HardSoftBigDecimalScore getScore() {
        return score;
    }

    public SolverStatus getSolverStatus() {
        return solverStatus;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }

    // 

}
