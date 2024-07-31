package org.arabius.platform.domain;

public class RoomPriority extends ArabiusEntity {
    private int roomId;
    private int priority;
    private String lessonType;

    // No-argument constructor
    public RoomPriority() {
    }

    public RoomPriority(int roomId, int priority, String lessonType) {
        this.roomId = roomId;
        this.priority = priority;
        this.lessonType = lessonType;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getPriority() {
        return priority;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    @Override
    public String toString() {
        return roomId + " @ " + priority;
    }
}
