package org.arabius.platform.solver.constraints;

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

}
