package org.arabius.platform.domain;

public class RoomPriority {
    private String roomId;
    private int priority;
    private String lessonType;

    // No-argument constructor
    public RoomPriority() {
    }

    public RoomPriority(String roomId, int priority, String lessonType) {
        this.roomId = roomId;
        this.priority = priority;
        this.lessonType = lessonType;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getPriority() {
        return priority;
    }

    public String getLessonType() {
        return lessonType;
    }

    @Override
    public String toString() {
        return roomId + " @ " + priority;
    }
}
