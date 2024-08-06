package org.arabius.platform.domain;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(scope = Room.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Room extends ArabiusEntity {

    @PlanningId
    private int id;

    private String name;
    private int capacity;
    private int branchId;

    private List<RoomPriority> roomPriorityList;

    public Room() {
        this.roomPriorityList = new ArrayList<>();
    }

    public Room(int id, String name, int capacity, int branchId) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.branchId = branchId;
        this.roomPriorityList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", floor=" + branchId +
                '}';
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public int getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBranchId() {
        return branchId;
    }

    public void addRoomPriority(RoomPriority roomPriority) {
        roomPriorityList.add(roomPriority);
    }

    public List<RoomPriority> getRoomPriorityList() {
        return roomPriorityList;
    }

    public int getRoomPriorityForLessonType(int lessonTypeId) {
        if (roomPriorityList == null) {
            return 100;
        }
        for (RoomPriority roomPriority : roomPriorityList) {
            if (roomPriority.getLessonTypeId() == lessonTypeId) {
                return roomPriority.getPriority();
            }
        }
        return 100;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setBranchId(int branch) {
        this.branchId = branch;
    }
}
