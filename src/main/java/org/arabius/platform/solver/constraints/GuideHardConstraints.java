package org.arabius.platform.solver.constraints;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.solver.justifications.GuideOnLevelJustification;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class GuideHardConstraints extends ArabiusConstraints {
    
    //guide constraints
    private Constraint guideConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.overlapping(Lesson::getBufferStart, Lesson::getBufferEnd),
                        Joiners.equal(Lesson::getGuide))
                .filter((lesson1, lesson2) -> lessonsAreInFuture(lesson1, lesson2))
                .penalize(HardSoftScore.ONE_HARD, (lesson1, lesson2) -> 100)
                .asConstraint("Guide conflict");
    }

    private Constraint guideCanDoLevel(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) && ! lesson.getGuide().getLevels().contains(lesson.getLevel()))
                .penalize(HardSoftScore.ONE_HARD, lesson -> 100)
                .justifyWith((lesson1, score) -> new GuideOnLevelJustification(lesson1))
                .asConstraint("Guide can do level");
    }

    private Constraint unAssignedGuide(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) && lesson.getGuide() == null && ! lesson.getLessonType().equals("Admin Hold"))
                .penalize(HardSoftScore.ONE_HARD, lesson -> lesson.getStudentCount())
                .asConstraint("No Unassigned Guide for client session");
    }
    //todo: make this calculate by closest matching time slot.  If exact match then no penalty.
    //if no exact match the penalty should be based minutes differece betten the two timeslots
    private Constraint guideOnSlot(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Lesson.class)
            .filter((lesson) -> lessonIsInFuture(lesson)
                && lesson.getSlotId() != null  
                && lesson.getGuide() != null  
                && lesson.getGuide().guideHasSlotOnDay(lesson.getDate(), lesson.getSlotId()))
            .penalize(HardSoftScore.ONE_HARD, lesson -> 100)
            .asConstraint("Guide on slot");
    }

    @Override
    public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                guideConflict(constraintFactory),
                guideCanDoLevel(constraintFactory),
                unAssignedGuide(constraintFactory),
                guideOnSlot(constraintFactory)
        };
    }
}
