package org.arabius.platform.solver.constraints;

import java.time.LocalDateTime;

import org.arabius.platform.domain.Lesson;

public abstract class ArabiusConstraints implements ConstraintProviderInterface {

    protected boolean lessonIsInFuture(Lesson lesson) {
        return lesson.getStartDateTime().isAfter(LocalDateTime.now());
    }

    protected boolean lessonsAreInFuture(Lesson lesson1, Lesson lesson2) {
        return lessonIsInFuture(lesson1) && lessonIsInFuture(lesson2);
    }

}
