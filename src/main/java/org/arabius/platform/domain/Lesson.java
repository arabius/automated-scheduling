package org.arabius.platform.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

@PlanningEntity
public class Lesson {

    @PlanningId
    private int id;

    private String level;
    private LocalDate date;
    private LocalDateTime bufferStart;
    private LocalDateTime bufferEnd;
    private String studentGroupHash;
    private String lessonType;
    private String guideName;
    private Timeslot timeslot;
    private int timeslotId;
    private int branch;

    @JsonIdentityReference
    @PlanningVariable (allowsUnassigned = true)
    private Guide guide;

    @JsonIdentityReference
    @PlanningVariable (allowsUnassigned = true)
    private Room room;

    
    public Lesson() {
    }

    public Lesson(int id, String level, LocalDate date, LocalDateTime bufferStart, LocalDateTime bufferEnd, String lessonType, String guideName, String studentGroupHash, int timeslotId, int branch) {
        this.id = id;
        this.date = date;
        this.level = level;
        this.bufferStart = bufferStart;
        this.bufferEnd = bufferEnd;
        this.lessonType = lessonType;
        this.guideName = guideName;
        this.studentGroupHash = studentGroupHash;
        this.timeslotId = timeslotId;
        this.branch = branch;
    }

    public Lesson(int id, LocalDate date, String level, LocalDateTime bufferStart, LocalDateTime bufferEnd, String lessonType, String guideName, String studentGroupHash, int timeslotId, int branch, Room room, Guide guide) {
        this(id, level, date, bufferStart, bufferEnd, lessonType, guideName, studentGroupHash, timeslotId, branch);
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

    public int getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getBufferStart() {
        return bufferStart;
    }

    public LocalDateTime getBufferEnd() {
        return bufferEnd;
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

    public String getLessonType() {
        return this.lessonType;
    }

    public String getGuideName() {
        return this.guideName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBufferStart(LocalDateTime bufferStart) {
        this.bufferStart = bufferStart;
    }

    public void setBufferEnd(LocalDateTime bufferEnd) {
        this.bufferEnd = bufferEnd;
    }

    public void setStudentGroupHash(String studentGroupHash) {
        this.studentGroupHash = studentGroupHash;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }



}
