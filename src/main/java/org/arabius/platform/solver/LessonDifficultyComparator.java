package org.arabius.platform.solver;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.arabius.platform.domain.Lesson;

//TODO make this work by number of hours/week and number of students then level of difficulty
public class LessonDifficultyComparator implements Comparator<Lesson> {
    @Override
    public int compare(Lesson lesson1, Lesson lesson2) {
        return new CompareToBuilder()
                .append(lesson1.getGuideStickiness()+lesson1.getStudentCount(), lesson2.getGuideStickiness()+lesson2.getStudentCount())
                .append(lesson1.getId(), lesson2.getId())
                .toComparison();
    }

}
