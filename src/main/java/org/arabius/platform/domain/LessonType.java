package org.arabius.platform.domain;

public class LessonType {
    
    private int id;

    private String name;
    private boolean enforceRoomCapacity;
    private GuideRequirment guideRequirment;
    private RoomRequirment roomRequirment;
    private boolean allowVideoCall;

    public LessonType() {
    }

    public LessonType(int id, String name, boolean enforceRoomCapacity, GuideRequirment guideRequirment, RoomRequirment roomRequirment, boolean allowVideoCall) {
        this.id = id;
        this.name = name;
        this.enforceRoomCapacity = enforceRoomCapacity;
        this.guideRequirment = guideRequirment;
        this.roomRequirment = roomRequirment;
        this.allowVideoCall = allowVideoCall;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEnforceRoomCapacity() {
        return enforceRoomCapacity;
    }

    public GuideRequirment getGuideRequirment() {
        return guideRequirment;
    }

    public RoomRequirment getRoomRequirment() {
        return roomRequirment;
    }

    public boolean isAllowVideoCall() {
        return allowVideoCall;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnforceRoomCapacity(boolean enforceRoomCapacity) {
        this.enforceRoomCapacity = enforceRoomCapacity;
    }

    public void setGuideRequirment(GuideRequirment guideRequirment) {
        this.guideRequirment = guideRequirment;
    }

    public void setRoomRequirment(RoomRequirment roomRequirment) {
        this.roomRequirment = roomRequirment;
    }

    public void setAllowVideoCall(boolean allowVideoCall) {
        this.allowVideoCall = allowVideoCall;
    }
}
