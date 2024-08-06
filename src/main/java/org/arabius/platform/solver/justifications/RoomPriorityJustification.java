package org.arabius.platform.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

import org.arabius.platform.domain.Lesson;

public record RoomPriorityJustification(Lesson lesson, String description)
                implements
                ConstraintJustification {

        public RoomPriorityJustification(Lesson lesson) {
                this(lesson,
                                "Lesson on '%s' for type '%s' has room priority '%s' for room '%s' with id '%s'"
                                                .formatted(lesson.getDate(),
                                                                lesson.getLessonTypeId(),
                                                                lesson.getRoom().getRoomPriorityForLessonType(
                                                                                lesson.getLessonTypeId()),
                                                                lesson.getRoom().getName(), lesson.getRoom().getId()));
        }
}
