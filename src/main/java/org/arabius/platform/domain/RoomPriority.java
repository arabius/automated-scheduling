package org.arabius.platform.domain;

public class RoomPriority extends ArabiusEntity {
    private int roomId;
    private int priority;
    private int lessonTypeId;

    // No-argument constructor
    public RoomPriority() {
    }

    public RoomPriority(int roomId, int priority, int lessonTypeId) {
        this.roomId = roomId;
        this.priority = priority;
        this.lessonTypeId = lessonTypeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getPriority() {
        return priority;
    }

    public int getLessonTypeId() {
        return lessonTypeId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setLessonTypeId(int lessonTypeId) {
        this.lessonTypeId = lessonTypeId;
    }

    @Override
    public String toString() {
        return roomId + " @ " + priority;
    }
}
