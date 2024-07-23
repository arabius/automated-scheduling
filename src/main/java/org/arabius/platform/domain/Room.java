package org.arabius.platform.domain;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(scope = Room.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Room {

    @PlanningId
    private int id;

    private String name;

    private int capacity;

    private int branch;

    private List<RoomPriority> roomPriorityList;

    public Room() {
    }

    public Room(int id, String name, int capacity, int branch) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.branch = branch;
        this.roomPriorityList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBranch() {
        return branch;
    }

    public void addRoomPriority(RoomPriority roomPriority) {
        roomPriorityList.add(roomPriority);
    }

    public List<RoomPriority> getRoomPriorityList() {
        return roomPriorityList;
    }

    public int getRoomPriorityForLessonType(String lessonType) {
        for (RoomPriority roomPriority : roomPriorityList) {
            if (roomPriority.getLessonType().equals(lessonType)) {
                return roomPriority.getPriority();
            }
        }
        return 100;
    }
}
