package org.arabius.platform.domain;

public class GuideRequirment extends ArabiusRequirment {
    public GuideRequirment() {
    }

    public GuideRequirment(String name, boolean canAssign, boolean isRequired, boolean isOptional) {
        super(name, canAssign, isRequired, isOptional);
    }
}
