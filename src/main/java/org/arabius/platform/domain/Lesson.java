package org.arabius.platform.domain;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

@PlanningEntity
public class Lesson {

    @PlanningId
    private String id;

    private String level;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private String studentGroupHash;
    private String lessonType;
    private String guideName;
    private Timeslot timeslot;
    private int timeslotId;
    private int branch;

    // @JsonIdentityReference
    // @PlanningVariable (allowsUnassigned = true)
    private Guide guide;

    @JsonIdentityReference
    @PlanningVariable (allowsUnassigned = true)
    private Room room;

    public Lesson() {
    }

    public Lesson(String id, String level, LocalDate date, LocalTime start, LocalTime end, String lessonType, String guideName, String studentGroupHash, int timeslotId, int branch) {
        this.id = id;
        this.date = date;
        this.level = level;
        this.start = start;
        this.end = end;
        this.lessonType = lessonType;
        this.guideName = guideName;
        this.studentGroupHash = studentGroupHash;
        this.timeslotId = timeslotId;
        this.branch = branch;
    }

    public Lesson(String id, LocalDate date, String level, LocalTime start, LocalTime end, String lessonType, String guideName, String studentGroupHash, int timeslotId, int branch, Room room, Guide guide) {
        this(id, level, date, start, end, lessonType, guideName, studentGroupHash, timeslotId, branch);
        this.room = room;
        this.guide = guide;
    }

    // @Override
    // public String toString() {
    //     return subject + "(" + id + ")";
    // }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public int getTimeslotId() {
        return timeslotId;
    }

    public int getBranch() {
        return branch;
    }

    public String getStudentGroupHash() {
        return studentGroupHash;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public Timeslot getTimeslot() {
        return this.timeslot;
    }

    public int getStudentCount() {
        return this.studentGroupHash.split(",").length;
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(this.date, this.start);
    }

    public String getLessonType() {
        return this.lessonType;
    }

    public String getGuideName() {
        return this.guideName;
    }

}
