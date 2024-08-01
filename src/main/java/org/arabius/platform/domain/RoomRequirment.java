package org.arabius.platform.domain;

public class RoomRequirment extends ArabiusRequirment {
    public RoomRequirment() {
    }

    public RoomRequirment(String key, boolean canAssign, boolean isRequired, boolean isOptional) {
        super(key, canAssign, isRequired, isOptional);
    }
}
