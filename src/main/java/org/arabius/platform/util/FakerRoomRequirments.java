package org.arabius.platform.util;

import java.util.ArrayList;

import org.arabius.platform.domain.RoomRequirment;

public class FakerRoomRequirments {
    private static ArrayList<RoomRequirment> roomRequirments;

    public FakerRoomRequirments() {
        roomRequirments = new ArrayList<>();
        roomRequirments.add(new RoomRequirment("no_room", false, false, false));
        roomRequirments.add(new RoomRequirment("room_optional", true, false, true));
        roomRequirments.add(new RoomRequirment("room_required", true, true, false));
    }

    public static ArrayList<RoomRequirment> getRoomRequirments() {
        return roomRequirments;
    }
}
