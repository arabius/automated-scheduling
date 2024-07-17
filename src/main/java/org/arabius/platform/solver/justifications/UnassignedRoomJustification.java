package org.arabius.platform.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

import org.arabius.platform.domain.Lesson;

public record UnassignedRoomJustification(Lesson lesson1, String description)
        implements
            ConstraintJustification {

    public UnassignedRoomJustification(Lesson lesson1) {
        this(lesson1,
                "Lesson on '%s' at '%s' for student group '%s' is unassigned to a room"
                        .formatted(lesson1.getDate(), lesson1.getStart(), lesson1.getStudentGroupHash()));
    }
}
