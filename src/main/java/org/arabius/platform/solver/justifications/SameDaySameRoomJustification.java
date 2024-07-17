package org.arabius.platform.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

import org.arabius.platform.domain.Lesson;

public record SameDaySameRoomJustification(Lesson lesson1, Lesson lesson2, String description)
        implements
            ConstraintJustification {

    public SameDaySameRoomJustification(Lesson lesson1, Lesson lesson2) {
        this(lesson1, lesson2,
                "Lesson on '%s' at '%s' for student group '%s' is not assigned the same room on '%s' at '%s' for student group '%s'."
                        .formatted(lesson1.getDate(), lesson1.getStart(), lesson1.getStudentGroupHash(), lesson2.getDate(),
                                lesson2.getStart(), lesson2.getStudentGroupHash()));
    }
}
