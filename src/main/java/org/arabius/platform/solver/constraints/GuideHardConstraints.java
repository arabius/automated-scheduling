package org.arabius.platform.solver.constraints;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.solver.justifications.GuideNotPresentJustification;
import org.arabius.platform.solver.justifications.GuideOnLevelJustification;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;
import ai.timefold.solver.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;

public class GuideHardConstraints extends ArabiusConstraints {

        // guide constraints
        private Constraint guideConflict(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEachUniquePair(Lesson.class,
                                                Joiners.equal(Lesson::getGuide),
                                                Joiners.equal(Lesson::getDate),
                                                Joiners.overlapping(Lesson::getBufferStart, Lesson::getBufferEnd))
                                .filter((lesson1, lesson2) -> areBothLessonsScheduled(lesson1, lesson2)
                                                && lesson1.getGuide() != null)
                                .penalize(HardSoftBigDecimalScore.ONE_HARD, (lesson1, lesson2) -> 100)
                                .asConstraint("Guide conflict");
        }

        private Constraint guideCanDoLevel(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEach(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson) && lesson.getGuide() != null
                                                && !lesson.getGuide().getLevels().contains(lesson.getLevel()))
                                .penalize(HardSoftBigDecimalScore.ONE_HARD, lesson -> 55)
                                .justifyWith((lesson1, score) -> new GuideOnLevelJustification(lesson1))
                                .asConstraint("Guide can do level");
        }

        private Constraint unAssignedGuide(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEachIncludingUnassigned(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson) && lesson.getGuide() == null &&
                                                lesson.isRequireGuide())
                                .penalize(HardSoftBigDecimalScore.ONE_HARD, lesson -> lesson.getStudentCount() * 2000)
                                .asConstraint("No Unassigned Guide for client session");
        }

        // todo: make this calculate by closest matching time slot. If exact match then
        // no penalty.
        // if no exact match the penalty should be based minutes differece betten the
        // two timeslots
        private Constraint guideIsPresentForSession(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEach(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson)
                                                && lesson.getSlotId() != null
                                                && lesson.getGuide() != null
                                                && lesson.getGuide().presentForLesson(lesson))
                                .penalize(HardSoftBigDecimalScore.ONE_HARD, lesson -> 100)
                                .justifyWith((lesson1, score) -> new GuideNotPresentJustification(lesson1))
                                .asConstraint("Guide is not present for session");
        }

        @Override
        public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
                return new Constraint[] {
                                unAssignedGuide(constraintFactory),
                                guideConflict(constraintFactory),
                                guideCanDoLevel(constraintFactory),
                                guideIsPresentForSession(constraintFactory)
                };

        }
}
