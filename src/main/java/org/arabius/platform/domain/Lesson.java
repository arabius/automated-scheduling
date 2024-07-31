package org.arabius.platform.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.arabius.platform.solver.LessonPinningFilter;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

@PlanningEntity(pinningFilter = LessonPinningFilter.class)
public class Lesson {

    @PlanningId
    private int id;

    private String level;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private LocalDateTime bufferStart;
    private LocalDateTime bufferEnd;
    private String studentGroupHash;
    private String lessonType;
    private Integer slotId;
    private int branchId;

    private Integer initialGuideId;
    private Integer initialRoomId;
    
    @JsonIdentityReference
    @PlanningVariable (allowsUnassigned = true)
    private Guide guide;

    @JsonIdentityReference
    @PlanningVariable (allowsUnassigned = true)
    private Room room;
    
    public Lesson() {
    }

    public Lesson(int id, String level, LocalDate date, LocalTime start, LocalTime end, LocalDateTime bufferStart, LocalDateTime bufferEnd, String lessonType, String studentGroupHash, Integer slotId, int branchId) {
        this.id = id;
        this.date = date;
        this.start = start;
        this.end = end;
        this.level = level;
        this.bufferStart = bufferStart;
        this.bufferEnd = bufferEnd;
        this.lessonType = lessonType;
        this.studentGroupHash = studentGroupHash;
        this.slotId = slotId;
        this.branchId = branchId;
    }

    public Lesson(int id, LocalDate date, LocalTime start, LocalTime end, String level, LocalDateTime bufferStart, LocalDateTime bufferEnd, String lessonType, String studentGroupHash, Integer slotId, Integer branchId, Integer initialGuideId, Integer initialRoomId) {
        this(id, level, date, start, end, bufferStart, bufferEnd, lessonType, studentGroupHash, slotId, branchId);
        this.initialGuideId = initialGuideId;
        this.initialRoomId = initialRoomId;
    }



    public Lesson(int id, LocalDate date, LocalTime start, LocalTime end, String level, LocalDateTime bufferStart, LocalDateTime bufferEnd, String lessonType, String studentGroupHash, Integer slotId, Integer branchId, Guide guide, Room room) {
        this(id, level, date, start, end, bufferStart, bufferEnd, lessonType, studentGroupHash, slotId, branchId);
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

    public Integer getSlotId() {
        return slotId;
    }

    public int getBranchId() {
        return branchId;
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

    public int getStudentCount() {
        return this.studentGroupHash.split(",").length;
    }

    public String getLessonType() {
        return this.lessonType;
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

    public void setBranchId(int branch) {
        this.branchId = branch;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public LocalTime getStart() {
        return start;
    }
    
    public void setStart(LocalTime start) {
        this.start = start;
    }
    
    public LocalTime getEnd() {
        return end;
    }
    
    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public Integer getInitialGuideId() {
        return initialGuideId;
    }

    public Integer getInitialRoomId() {
        return initialRoomId;
    }

    public void setInitialGuideId(Integer initialGuideId) {
        this.initialGuideId = initialGuideId;
    }

    public void setInitialRoomId(Integer initialRoomId) {
        this.initialRoomId = initialRoomId;
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, start);
    }
}
