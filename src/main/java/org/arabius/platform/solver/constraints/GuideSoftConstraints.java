package org.arabius.platform.solver.constraints;

import java.util.Objects;

import org.arabius.platform.domain.Lesson;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class GuideSoftConstraints extends ArabiusConstraints {
    private Constraint consecutiveLessonsWithSameGuide(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.equal(Lesson::getLevel), // same level
                        Joiners.lessThan(Lesson::getBufferStart)) 
                .filter((lesson1, lesson2) -> !areNeitherLessonsScheduled(lesson1, lesson2) && !Objects.equals(lesson1.getGuide(), lesson2.getGuide()))
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> { return 1 * lesson1.getGuideStickiness(); })
                .asConstraint("Consecutive sessions without same guide");
    }

    private Constraint guideInSameRoomForSameDayLessons(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getDate),        
                        Joiners.equal(Lesson::getGuide)                        
                        )
                .filter((lesson1, lesson2) -> areBothLessonsScheduled(lesson1, lesson2) && !Objects.equals(lesson1.getRoom(), lesson2.getRoom()))
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> 5)
                .asConstraint("Guide in same room for same day lessons");
    }

    @Override
    public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                consecutiveLessonsWithSameGuide(constraintFactory),
               // guideInSameRoomForSameDayLessons(constraintFactory)
        };
    }
}
