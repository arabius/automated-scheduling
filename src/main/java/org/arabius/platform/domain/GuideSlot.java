package org.arabius.platform.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuideSlot {
    private LocalDate date;
    private int guideId;
    private List<Integer> slotIds;
    private LocalTime startTime;
    private LocalTime endTime;

    public GuideSlot() {
    }

    public GuideSlot(LocalDate date, int guideId, List<Integer> slotIds, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.guideId = guideId;
        this.slotIds = slotIds;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public GuideSlot(LocalDate date, int guideId, String slotIds, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.guideId = guideId;
        this.slotIds = this.parseStringToIntList(slotIds);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private List<Integer> parseStringToIntList(String stringToParse) {
        return Arrays.stream(stringToParse.split("\\|"))
                                     .map(Integer::parseInt)
                                     .collect(Collectors.toList());
    }

    @JsonProperty("date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @JsonProperty("guide_id")
    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }
    
    public List<Integer> getSlotIds() {
        return slotIds;
    }

    @JsonProperty("slot_ids")
    public String getSlotIdsAsString() {
        return slotIds.stream().map(Object::toString).collect(Collectors.joining("|"));
    }

    public void setSlotIds(List<Integer> slotIds) {
        this.slotIds = slotIds;
    }

    public void setSlotIds(String slotIds) {
        this.slotIds = this.parseStringToIntList(slotIds);
    }

    @JsonProperty("start_time")
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("end_time")
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

        // Override toString if necessary
        @Override
        public String toString() {
            return date + " " + guideId;
        }
}
