package org.arabius.platform.solver;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.domain.Timetable;

import ai.timefold.solver.core.api.domain.entity.PinningFilter;

import java.time.LocalDateTime;

public class LessonPinningFilter implements PinningFilter<Timetable, Lesson> {

	@Override
	public boolean accept(Timetable timetable, Lesson lesson) {
		// Get the current time
		LocalDateTime now = LocalDateTime.now();
		// Check if the lesson's start time is before the current time
		return lesson.getStartDateTime().isBefore(now);
	}
}