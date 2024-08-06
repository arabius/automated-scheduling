package org.arabius.platform.domain;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.arabius.platform.solver.LessonDifficultyComparator;
import org.arabius.platform.solver.LessonPinningFilter;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@PlanningEntity(pinningFilter = LessonPinningFilter.class, difficultyComparatorClass = LessonDifficultyComparator.class)
public class Lesson extends ArabiusEntity {

    @PlanningId
    private int id;

    private Integer level;
    private String status;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private LocalDateTime bufferStart;
    private LocalDateTime bufferEnd;
    private String studentGroupHash;
    private Integer slotId;
    private int branchId;
    private int studentCount;

    // lesson type config
    private boolean allowGuide;
    private boolean allowRoom;
    private boolean allowVideoCall;
    private boolean enforceRoomCapacity;
    private boolean requireGuide;
    private boolean requireRoom;
    private boolean levelIsDifficult;
    private int guideStickiness;

    @JsonIgnore
    private ArrayList<Integer> allowedGuideIds = new ArrayList<>();

    private Integer initialGuideId;
    private Integer initialRoomId;
    private int lessonTypeId;

    @JsonIdentityReference
    @PlanningVariable // (allowsUnassigned = true)
    private Guide guide;

    @JsonIdentityReference
    @PlanningVariable // (allowsUnassigned = true)
    private Room room;

    public Lesson() {
    }

    public Lesson(int id, Integer level, LocalDate date, LocalTime start, LocalTime end, LocalDateTime bufferStart,
            LocalDateTime bufferEnd, String studentGroupHash, Integer slotId, int branchId, int lessonTypeId,
            String allowedGuideIds, String status, int studentCount) {
        this.id = id;
        this.date = date;
        this.start = start;
        this.end = end;
        this.level = level;
        this.bufferStart = bufferStart;
        this.bufferEnd = bufferEnd;
        this.studentGroupHash = studentGroupHash;
        this.slotId = slotId;
        this.branchId = branchId;
        this.lessonTypeId = lessonTypeId;
        this.allowedGuideIds = this.parseStringToIntList(allowedGuideIds);
        this.status = status;
        this.studentCount = studentCount;
    }

    public Lesson(int id, LocalDate date, LocalTime start, LocalTime end, Integer level, LocalDateTime bufferStart,
            LocalDateTime bufferEnd, String studentGroupHash, Integer slotId, int branchId, int lessonTypeId,
            String allowedGuideIds, Integer initialGuideId, Integer initialRoomId, String status, int studentCount) {
        this(id, level, date, start, end, bufferStart, bufferEnd, studentGroupHash, slotId, branchId, lessonTypeId,
                allowedGuideIds, status, studentCount);
        this.initialGuideId = initialGuideId;
        this.initialRoomId = initialRoomId;
        this.studentCount = this.studentGroupHash.split(",").length;
    }

    public Lesson(int id, LocalDate date, LocalTime start, LocalTime end, Integer level, LocalDateTime bufferStart,
            LocalDateTime bufferEnd, String studentGroupHash, Integer slotId, int branchId, int lessonTypeId,
            String allowedGuideIds, Guide guide, Room room, String status, int studentCount) {
        this(id, level, date, start, end, bufferStart, bufferEnd, studentGroupHash, slotId, branchId, lessonTypeId,
                allowedGuideIds, status, studentCount);
        this.room = room;
        this.guide = guide;
        this.studentCount = this.studentGroupHash.split(",").length;
    }

    // @Override
    // public String toString() {
    // return subject + "(" + id + ")";
    // }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public int getId() {
        return id;
    }

    public Integer getLevel() {
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
        return this.studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(Integer level) {
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

    @JsonProperty("startDateTime")
    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, start);
    }

    public ArrayList<Integer> getAllowedGuideIds() {
        return allowedGuideIds;
    }

    public void setAllowedGuideIds(ArrayList<Integer> allowedGuideIds) {
        this.allowedGuideIds = allowedGuideIds;
    }

    public void setAllowedGuideIds(String allowedGuideIds) {
        this.allowedGuideIds = this.parseStringToIntList(allowedGuideIds);
    }

    public boolean isAllowGuide() {
        return allowGuide;
    }

    public void setAllowGuide(boolean allowGuide) {
        this.allowGuide = allowGuide;
    }

    public boolean isAllowRoom() {
        return allowRoom;
    }

    public void setAllowRoom(boolean allowRoom) {
        this.allowRoom = allowRoom;
    }

    public boolean isAllowVideoCall() {
        return allowVideoCall;
    }

    public void setAllowVideoCall(boolean allowVideoCall) {
        this.allowVideoCall = allowVideoCall;
    }

    public boolean isEnforceRoomCapacity() {
        return enforceRoomCapacity;
    }

    public void setEnforceRoomCapacity(boolean enforceRoomCapacity) {
        this.enforceRoomCapacity = enforceRoomCapacity;
    }

    public boolean isRequireGuide() {
        return requireGuide;
    }

    public void setRequireGuide(boolean requireGuide) {
        this.requireGuide = requireGuide;
    }

    public boolean isRequireRoom() {
        return requireRoom;
    }

    public void setRequireRoom(boolean requireRoom) {
        this.requireRoom = requireRoom;
    }

    public boolean isLevelIsDifficult() {
        return levelIsDifficult;
    }

    public void setLevelIsDifficult(boolean levelIsDifficult) {
        this.levelIsDifficult = levelIsDifficult;
    }

    public int getGuideStickiness() {
        return guideStickiness;
    }

    public void setGuideStickiness(int guideStickiness) {
        this.guideStickiness = guideStickiness;
    }

    public int getLessonTypeId() {
        return lessonTypeId;
    }

    public void setLessonTypeId(int lessonTypeId) {
        this.lessonTypeId = lessonTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDurationInMinutes() {
        Duration duration = Duration.between(start, end);
        return duration.toMinutes();
    }

    public int getRoomPriorityForLessonType() {
        Room room = this.getRoom();
        if (room == null) {
            return 100; // Default value if room is null
        }
        return room.getRoomPriorityForLessonType(lessonTypeId);
    }
}
