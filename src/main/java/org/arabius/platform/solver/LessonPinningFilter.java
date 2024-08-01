package org.arabius.platform.solver;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.domain.Timetable;

import ai.timefold.solver.core.api.domain.entity.PinningFilter;

public class LessonPinningFilter implements PinningFilter<Timetable, Lesson> {

	@Override
	public boolean accept(Timetable timetable, Lesson lesson) {
		return ! lesson.getStatus().equals("scheduled");
	}
}