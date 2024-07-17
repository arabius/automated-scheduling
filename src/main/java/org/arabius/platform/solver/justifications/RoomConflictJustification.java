package org.arabius.platform.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.domain.Room;

public record RoomConflictJustification(Room room, Lesson lesson1, Lesson lesson2, String description)
        implements
            ConstraintJustification {

    public RoomConflictJustification(Room room, Lesson lesson1, Lesson lesson2) {
        this(room, lesson1, lesson2,
                "Room '%s' is used for lesson '%s' for student group '%s' and lesson '%s' for student group '%s' at '%s %s'"
                        .formatted(room, lesson1.getLevel(), lesson1.getStudentGroupHash(), lesson2.getLevel(),
                                lesson2.getStudentGroupHash(), lesson1.getDate(),
                                lesson1.getDate()));
    }
}
