package org.arabius.platform.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(scope = Guide.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Guide extends ArabiusEntity {
    
    @PlanningId
    private int id;
    private String name;

    private List<GuideSlot> guideSlots = new ArrayList<>();
    private List<String> levels;

    public Guide() {
        this.guideSlots = new ArrayList<>(); // Initialize guideSlots
    }

    public Guide(int id, String name, String levels) {
        this.id = id;
        this.name = name;
        this.levels = this.parseStringToStringList(levels);
        this.guideSlots = new ArrayList<>(); // Initialize guideSlots
    }

    public Guide(int id, String name, List<String> levels, List<GuideSlot> guideSlots) {
        this.id = id;
        this.name = name;
        this.guideSlots = guideSlots != null ? guideSlots : new ArrayList<>();
        this.levels = levels;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public List<GuideSlot> getGuideSlots() {
        return guideSlots;
    }

    @JsonProperty("levels")
    public String getLevelsAsString() {
        return levels.stream().collect(Collectors.joining("|"));
    }

    public String getGuideSlotsAsString() {
        String guideString = guideSlots.stream().map(GuideSlot::toString).collect(Collectors.joining("|"));
        return guideString;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setGuideSlots(List<GuideSlot> guideSlots) {
        this.guideSlots = guideSlots;
    }

    public void addGuideSlot(GuideSlot guideSlot) {
        this.guideSlots.add(guideSlot);
    }

    public void addMatchingGuideslots(List<GuideSlot> guideSlots) {
        for (GuideSlot guideSlot : guideSlots) {
            if (guideSlot.getGuideId() == this.id) {
                this.addGuideSlot(guideSlot);
            }
        }
    }

    public boolean guideHasSlotOnDay(LocalDate date, int slotId) {
        for (GuideSlot guideSlot : this.guideSlots) {
            if (guideSlot.getDate().equals(date) && guideSlot.getSlotIds().contains(slotId)) {
                return true;
            }
        }
        return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevels(String levels) {
        this.levels = this.parseStringToStringList(levels);
    }

 
}
