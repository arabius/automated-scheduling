package org.arabius.platform.solver.constraints;

import java.time.temporal.ChronoUnit;

import org.arabius.platform.domain.Lesson;

public abstract class ArabiusConstraints implements ConstraintProviderInterface {

    protected boolean isLessonScheduled(Lesson lesson) {
        return lesson.getStatus().equals("scheduled");
    }

    protected boolean areBothLessonsScheduled(Lesson lesson1, Lesson lesson2) {
        return isLessonScheduled(lesson1) && isLessonScheduled(lesson2);
    }

    protected boolean areNeitherLessonsScheduled(Lesson lesson1, Lesson lesson2) {
        return !isLessonScheduled(lesson1) && !isLessonScheduled(lesson2);
    }

    protected long getLessonDaysApart(Lesson lesson1, Lesson lesson2) {
       return Math.abs(ChronoUnit.DAYS.between(lesson1.getDate(), lesson2.getDate()));
    }

    protected boolean areLessonsWithinXDays(Lesson lesson1, Lesson lesson2, long days) {
        return getLessonDaysApart(lesson1, lesson2) <= days;
    }

}
