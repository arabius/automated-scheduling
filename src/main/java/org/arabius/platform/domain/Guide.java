package org.arabius.platform.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private int schedulingCost;

    private ArrayList<GuideSlot> guideSlots = new ArrayList<>();
    private ArrayList<GuideAbsence> guideAbsences = new ArrayList<>();
    private ArrayList<Integer> levels = new ArrayList<>();

    public Guide() {
        this.guideSlots = new ArrayList<>(); // Initialize guideSlots
        this.levels = new ArrayList<>(); // Initialize levels
    }

    public Guide(int id, String name, String levels, int schedulingCost) {
        this.id = id;
        this.name = name;
        this.levels = this.parseStringToIntList(levels);
        this.schedulingCost = schedulingCost;
        this.guideSlots = new ArrayList<>(); // Initialize guideSlots
    }

    public Guide(int id, String name, ArrayList<Integer> levels, ArrayList<GuideSlot> guideSlots,
            ArrayList<GuideAbsence> guideAbsences, int schedulingCost) {
        this.id = id;
        this.name = name;
        this.levels = levels;
        this.guideSlots = guideSlots != null ? guideSlots : new ArrayList<>();
        this.guideAbsences = guideAbsences != null ? guideAbsences : new ArrayList<>();
        this.schedulingCost = schedulingCost;
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
        String levelsString = levels.stream()
                .map(Object::toString)
                .collect(Collectors.joining("|"));
        return levelsString;
    }

    public String getGuideSlotsAsString() {
        String guideString = guideSlots.stream().map(GuideSlot::toString).collect(Collectors.joining("|"));
        return guideString;
    }

    public ArrayList<Integer> getLevels() {
        return levels;
    }

    public void setGuideSlots(ArrayList<GuideSlot> guideSlots) {
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

    public boolean canGuideDoLevel(int level) {
        return this.levels.contains(level);
    }

    public boolean guideHasSlotOnDay(LocalDate date, int slotId) {
        for (GuideSlot guideSlot : this.guideSlots) {
            if (guideSlot.getDate().equals(date) && guideSlot.getSlotIds().contains(Integer.valueOf(slotId))) {
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
        this.levels = this.parseStringToIntList(levels);
    }

    public void setGuideAbsences(ArrayList<GuideAbsence> guideAbsences) {
        this.guideAbsences = guideAbsences;
    }

    public void addGuideAbsence(GuideAbsence guideAbsence) {
        this.guideAbsences.add(guideAbsence);
    }

    public void addMatchingGuideAbsences(List<GuideAbsence> guideAbsences) {
        for (GuideAbsence guideAbsence : guideAbsences) {
            if (guideAbsence.getGuideId() == this.id) {
                this.addGuideAbsence(guideAbsence);
            }
        }
    }

    public boolean guideHasOverlappingAbsence(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        for (GuideAbsence guideAbsence : this.guideAbsences) {
            if (guideAbsence.isOverlappingTimes(startDateTime, endDateTime)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<GuideAbsence> getGuideAbsences() {
        return guideAbsences;
    }

    public int getSchedulingCost() {
        return schedulingCost;
    }

    public void setSchedulingCost(int schedulingCost) {
        this.schedulingCost = schedulingCost;
    }

    public boolean presentForLesson(Lesson lesson) {
        return this.guideHasSlotOnDay(lesson.getDate(), lesson.getSlotId());
        // || this.guideHasOverlappingAbsence(lesson.getBufferStart(),
        // lesson.getBufferEnd());
    }

}
