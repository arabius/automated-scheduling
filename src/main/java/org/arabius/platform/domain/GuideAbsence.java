package org.arabius.platform.domain;

import java.time.LocalDateTime;

public class GuideAbsence {
    private int id;
    private int guideId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public GuideAbsence() {
    }

    public GuideAbsence(int id, int guideId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.guideId = guideId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isOverlappingTimes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return this.startDateTime.isBefore(endDateTime) && this.endDateTime.isAfter(startDateTime);
    }
}
