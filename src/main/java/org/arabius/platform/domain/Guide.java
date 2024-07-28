package org.arabius.platform.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(scope = Guide.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Guide {
    
    @PlanningId
    private int id;
    private String name;
    private List<Timeslot> timeSlots;
    private List<Integer> timeSlotIds;
    private List<String> levels;

    public Guide() {
    }

    public Guide(int id, String name, String levels, String timeSlotIds) {
        this.id = id;
        this.name = name;
        this.timeSlotIds = this.parseStringToIntList(timeSlotIds);
        this.levels = this.parseStringToStringList(levels);
    }

    public Guide(int id, String name, List<String> levels, List<Integer> timeSlotIds) {
        this.id = id;
        this.name = name;
        this.timeSlotIds = timeSlotIds;
        this.levels = levels;
    }

    private List<Integer> parseStringToIntList(String stringToParse) {
        // //check for null and empty string
        // if (stringToParse == null || stringToParse.isEmpty()) {
        //     return null;
        // }
        return Arrays.stream(stringToParse.split("\\|"))
                                     .map(Integer::parseInt)
                                     .collect(Collectors.toList());
    }

    private List<String> parseStringToStringList(String stringToParse) {
        // //check for null and empty string
        // if (stringToParse == null || stringToParse.isEmpty()) {
        //     return null;
        // }
        return Arrays.asList(stringToParse.split("\\|"));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Timeslot> getTimeSlots() {
        return timeSlots;
    }

    public List<Integer> getTimeSlotIds() {
        return timeSlotIds;
    }

    @JsonProperty("timeSlotIds")
    public String getTimeSlotIdsAsString() {
        return timeSlotIds.stream().map(Object::toString).collect(Collectors.joining("|"));
    }

    @JsonProperty("levels")
    public String getLevelsAsString() {
        return levels.stream().collect(Collectors.joining("|"));
    }

    public List<String> getLevels() {
        return levels;
    }
    
    public void setTimeSlots(List<Timeslot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void addTimeSlot(Timeslot timeSlot) {
        this.timeSlots.add(timeSlot);
    }

    public void addMatchingTimeSlots(List<Timeslot> timeSlots) {
        for (Timeslot timeSlot : timeSlots) {
            if (this.timeSlotIds.contains(timeSlot.getId())) {
                this.timeSlots.add(timeSlot);
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("timeSlotIds")
    public void setTimeSlotIds(String timeSlotIds) {
        this.timeSlotIds =  this.parseStringToIntList(timeSlotIds);
    }

    public void setLevels(String levels) {
        this.levels = this.parseStringToStringList(levels);
    }



}
