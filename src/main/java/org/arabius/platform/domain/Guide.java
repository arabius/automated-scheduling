package org.arabius.platform.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

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
        //check for null and empty string
        if (stringToParse == null || stringToParse.isEmpty()) {
            return null;
        }
        return Arrays.stream(stringToParse.split("\\|"))
                                     .map(Integer::parseInt)
                                     .collect(Collectors.toList());
    }

    private List<String> parseStringToStringList(String stringToParse) {
        //check for null and empty string
        if (stringToParse == null || stringToParse.isEmpty()) {
            return null;
        }
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

    public void setTimeSlotIds(String timeSlotIds) {
        this.timeSlotIds =  this.parseStringToIntList(timeSlotIds);
    }

    public void setLevels(String levels) {
        this.levels = this.parseStringToStringList(levels);
    }



}
