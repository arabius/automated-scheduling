package org.arabius.platform.solver.constraints;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.arabius.platform.domain.Lesson;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class GuideSoftConstraints implements ConstraintProviderInterface {
    private Constraint consecutiveLessonsWithSameGuide(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.equal(Lesson::getLevel), // same level
                        Joiners.lessThan(Lesson::getBufferStart)) 
                .filter((lesson1, lesson2) -> !lesson1.getGuide().equals(lesson2.getGuide()) )
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> {
                        String levels = "1|1 Business|1A Hotels|2A|2B|2C|3A|3B|3C|4A|4B|5A|5B|Admin Meeting|Free Trial|Level 2 Bridge|Mini-Session";
                        // Split the string into an array
                        String[] levelsArray = levels.split("\\|");

                        // The element you are looking for
                        String target = lesson1.getLevel();
                        LocalDate date1 = lesson1.getDate();
                        LocalDate date2 = lesson2.getDate();
                        int daysBetween = (int) ChronoUnit.DAYS.between(date1, date2);

                        // Find the index of the target element
                        int index = 0;
                        for (int i = 0; i < levelsArray.length; i++) {
                                if (levelsArray[i].equals(target)) {
                                        index = i;
                                        break;
                                }
                        }
                        if (daysBetween != 0 && index != 0) {
                            return 100 / Math.abs(daysBetween + 1) / Math.abs(index + 1);
                        } else {
                            return 100; // or any other appropriate value
                        }
                })
                .asConstraint("Consecutive sessions without same guide");
    }

    @Override
    public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                consecutiveLessonsWithSameGuide(constraintFactory)
        };
    }
}
