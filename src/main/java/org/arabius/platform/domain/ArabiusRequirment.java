package org.arabius.platform.domain;

public abstract class ArabiusRequirment {

    protected String key;
    protected boolean canAssign;
    protected boolean isRequired;
    protected boolean isOptional;

    public ArabiusRequirment() {
    }

    public ArabiusRequirment(String key, boolean canAssign, boolean isRequired, boolean isOptional) {
        this.key = key;
        this.canAssign = canAssign;
        this.isRequired = isRequired;
        this.isOptional = isOptional;
    }

    public String getKey() {
        return key;
    }

    public boolean isCanAssign() {
        return canAssign;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public boolean isOptional() {
        return isOptional;
    }

}
