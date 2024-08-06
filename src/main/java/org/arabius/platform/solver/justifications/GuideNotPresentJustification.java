package org.arabius.platform.solver.justifications;

import org.arabius.platform.domain.Lesson;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

public record GuideNotPresentJustification(Lesson lesson1, String description)
        implements
        ConstraintJustification {

    public GuideNotPresentJustification(Lesson lesson1) {
        this(lesson1,
                "Lesson on '%s' at '%s' assigned to guide '%s' is not present for the session.  Slot id is '%s'.  Guide has overlapping absence: '%s'"
                        .formatted(lesson1.getDate(), lesson1.getBufferStart(), lesson1.getGuide().getName(),
                                lesson1.getSlotId(), lesson1.getGuide()
                                        .guideHasOverlappingAbsence(lesson1.getBufferStart(), lesson1.getBufferEnd())));
    }

}
