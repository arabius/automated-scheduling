package org.arabius.platform.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

import org.arabius.platform.domain.Lesson;

public record BigOnlineRoomJustification(Lesson lesson1, String description)
        implements
            ConstraintJustification {

    public BigOnlineRoomJustification(Lesson lesson1) {
        this(lesson1,
                "Lesson on '%s' at '%s' for student group '%s' with '%s' students is to a room with a capacity of '%s'"
                        .formatted(lesson1.getDate(), lesson1.getStart(), lesson1.getStudentGroupHash(), lesson1.getStudentCount(), lesson1.getRoom().getCapacity()));
    }
}
