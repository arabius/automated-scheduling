package org.arabius.platform.solver.constraints;

import java.math.BigDecimal;
import java.util.Objects;

import org.arabius.platform.domain.Guide;
import org.arabius.platform.domain.Lesson;

import java.math.BigDecimal;
import java.time.Duration;

import ai.timefold.solver.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;
import ai.timefold.solver.core.api.score.stream.common.LoadBalance;

public class GuideSoftConstraints extends ArabiusConstraints {
    private Constraint consecutiveLessonsWithSameGuide(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.equal(Lesson::getLevel), // same level
                        Joiners.lessThan(Lesson::getBufferStart)) 
                .filter((lesson1, lesson2) -> !areNeitherLessonsScheduled(lesson1, lesson2) && areLessonsWithinXDays(lesson1, lesson2, 14) && !Objects.equals(lesson1.getGuide(), lesson2.getGuide()))
                .penalize(HardSoftBigDecimalScore.ONE_SOFT, (lesson1, lesson2) -> { return Math.max(0, 14 - (int) getLessonDaysApart(lesson1, lesson2)) * lesson1.getGuideStickiness(); })
                .asConstraint("Consecutive sessions without same guide");
    }

    private Constraint guideInSameRoomForSameDayLessons(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getDate),        
                        Joiners.equal(Lesson::getGuide)                        
                        )
                .filter((lesson1, lesson2) -> areBothLessonsScheduled(lesson1, lesson2) && !Objects.equals(lesson1.getRoom(), lesson2.getRoom()))
                .penalize(HardSoftBigDecimalScore.ONE_SOFT, (lesson1, lesson2) -> 5)
                .asConstraint("Guide in same room for same day lessons");
    }

    Constraint fairAssignments(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
            .filter((lesson) -> isLessonScheduled(lesson))
            .groupBy(ConstraintCollectors.loadBalance(Lesson::getGuide, Lesson::getDurationInMinutes))
            .penalizeBigDecimal(HardSoftBigDecimalScore.ONE_SOFT, LoadBalance::unfairness)
            .asConstraint("fairAssignments");
   }

    @Override
    public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                consecutiveLessonsWithSameGuide(constraintFactory),
                fairAssignments(constraintFactory),
               // guideInSameRoomForSameDayLessons(constraintFactory)
        };
    }

}
