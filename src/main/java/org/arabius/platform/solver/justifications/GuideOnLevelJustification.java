package org.arabius.platform.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

import org.arabius.platform.domain.Lesson;

public record GuideOnLevelJustification(Lesson lesson1, String description)
        implements
            ConstraintJustification {

    public GuideOnLevelJustification(Lesson lesson1) {
        this(lesson1,
                "Lesson on %s %s has '%s' level but guide '%s' can't do that level '%s'."
                        .formatted(lesson1.getDate(), lesson1.getBufferStart(),  lesson1.getLevel(), lesson1.getGuide().getName(), lesson1.getGuide().getLevels()));
    }
}
