package org.arabius.platform.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;


import org.arabius.platform.domain.Lesson;
import org.arabius.platform.solver.justifications.RoomConflictJustification;
import org.arabius.platform.solver.justifications.SameDaySameRoomJustification;
import org.arabius.platform.solver.justifications.SameRoomJustification;
import org.arabius.platform.solver.justifications.SmallRoomJustification;
import org.arabius.platform.solver.justifications.BigOnlineRoomJustification;
import org.arabius.platform.solver.justifications.BigRoomJustification;
import org.arabius.platform.solver.justifications.GuideOnLevelJustification;
import org.arabius.platform.solver.justifications.UnassignedRoomJustification;


public class TimetableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
                roomConflict(constraintFactory),
                UnAssignedRoom(constraintFactory),
                 // Soft constraints
                roomCapacityLessThanStudentCount(constraintFactory),
                roomCapacityGreaterThanStudentCount(constraintFactory),
                roomCapacityGreaterThanTwoForOnline(constraintFactory),
                useRoomPriorityByLessonType(constraintFactory),
                consecutiveLessonsInSameRoom(constraintFactory),
                consecutiveSameDayLessonsInSameRoom(constraintFactory)

                // //guide hard
                // guideConflict(constraintFactory),
                // guideOnSlot(constraintFactory),
                // guideCanDoLevel(constraintFactory),
                // UnAssignedGuide(constraintFactory), 
                // //guide soft               
                // consecutiveLessonsWithSameGuide(constraintFactory),
        };
    }

    Constraint roomConflict(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                // Select each pair of 2 different lessons ...
                .forEachUniquePair(Lesson.class,
                        // ... in the same date ...
                        Joiners.equal(Lesson::getDate),
                        // ... overlapping in time ...
                        Joiners.overlapping(Lesson::getStart, Lesson::getEnd),
                        // ... in the same room ...
                        Joiners.equal(Lesson::getRoom))
                // ... and penalize each pair with a hard weight.
                .penalize(HardSoftScore.ONE_HARD)
                .justifyWith((lesson1, lesson2, score) -> new RoomConflictJustification(lesson1.getRoom(), lesson1, lesson2))
                .asConstraint("Room conflict");
    }

    Constraint UnAssignedRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> lesson.getRoom() == null && ! lesson.getLessonType().equals("Admin Hold"))
                .penalize(HardSoftScore.ONE_HARD)
                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                .asConstraint("No Unassigned room for client session");
    }

    Constraint UnAssignedRoomAdmin(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> lesson.getRoom() == null && lesson.getLessonType().equals("Admin Hold"))
                .penalize(HardSoftScore.ONE_SOFT)
                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                .asConstraint("Unassigned room for admin hold session");
    }

    Constraint roomCapacityLessThanStudentCount(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lesson.getRoom().getCapacity() < lesson.getStudentCount())
                .filter((lesson) -> ! lesson.getLessonType().equals("Online"))
                .filter((lesson) -> ! lesson.getLessonType().equals("On-site"))
                .filter((lesson) -> ! lesson.getLessonType().equals("Admin Hold"))
                .penalize(HardSoftScore.ONE_HARD)
                .justifyWith((lesson1, score) -> new SmallRoomJustification(lesson1))
                .asConstraint("Room capacity less than student count");
    }

    Constraint roomCapacityGreaterThanStudentCount(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lesson.getRoom().getCapacity()-1 > lesson.getStudentCount() && lesson.getLessonType().equals("In-person"))
                .penalize(HardSoftScore.ONE_SOFT, lesson -> lesson.getRoom().getCapacity() - lesson.getStudentCount())
                .justifyWith((lesson1, score) -> new BigRoomJustification(lesson1))
                .asConstraint("Room capacity greater than student count");
    }

    Constraint roomCapacityGreaterThanTwoForOnline(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lesson.getRoom().getCapacity() > 2)
                .filter((lesson) -> lesson.getLessonType().equals("Online"))
                .penalize(HardSoftScore.ONE_SOFT, lesson -> lesson.getRoom().getCapacity()*2)
                .justifyWith((lesson1, score) -> new BigOnlineRoomJustification(lesson1))
                .asConstraint("Room capacity greater than two for online lessons");
    }

    Constraint useRoomPriorityByLessonType(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .penalize(HardSoftScore.ONE_SOFT, lesson -> (int) lesson.getRoom().getRoomPriorityForLessonType(lesson.getLessonType()))
                .asConstraint("Use room priority by lesson type");
    }

    Constraint consecutiveLessonsInSameRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.lessThan(Lesson::getStartDateTime))
                .filter((lesson1, lesson2) -> !lesson1.getRoom().equals(lesson2.getRoom()) && lesson1.getLessonType().equals("In-person") && lesson2.getLessonType().equals("In-person"))
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> 1)
                .justifyWith((lesson1, lesson2, score) -> new SameRoomJustification(lesson1, lesson2))
                .asConstraint("Consecutive in-person lessons for the same clients should be in the same room");
    }

    Constraint consecutiveSameDayLessonsInSameRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.lessThan(Lesson::getStartDateTime),
                        Joiners.equal(Lesson::getDate)) 
                .filter((lesson1, lesson2) -> !lesson1.getRoom().equals(lesson2.getRoom()) && lesson1.getLessonType().equals("In-person") && lesson2.getLessonType().equals("In-person"))
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> 30)
                .justifyWith((lesson1, lesson2, score) -> new SameDaySameRoomJustification(lesson1, lesson2))
                .asConstraint("Consecutive Same-day in-person lessons for the same clients should be in the same room");
    }

    //guide constraints
    Constraint guideConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getDate),
                        Joiners.overlapping(Lesson::getStart, Lesson::getEnd),
                        Joiners.equal(Lesson::getGuide))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Guide conflict");
    }

    Constraint guideOnSlot(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> ! lesson.getGuide().getTimeSlotIds().contains(lesson.getTimeslotId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Guide on slot");
    }

    Constraint consecutiveLessonsWithSameGuide(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.lessThan(Lesson::getStartDateTime)) 
                .filter((lesson1, lesson2) -> !lesson1.getGuide().equals(lesson2.getGuide()))
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> {
                        String levels = "1|1 Business|1A Hotels|2A|2B|2C|3A|3B|3C|4A|4B|5A|5B|Admin Meeting|Free Trial|Level 2 Bridge|Mini-Session";
                        // Split the string into an array
                        String[] levelsArray = levels.split("\\|");

                        // The element you are looking for
                        String target = lesson1.getLevel();

                        // Find the index of the target element
                        int index = -1;
                        for (int i = 0; i < levelsArray.length; i++) {
                                if (levelsArray[i].equals(target)) {
                                        index = i;
                                        break;
                                }
                        }
                        return 17 - index;
                })
                .asConstraint("Consecutive sessions without same guide");
    }

    Constraint guideCanDoLevel(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> ! lesson.getGuide().getLevels().contains(lesson.getLevel()))
                .penalize(HardSoftScore.ONE_HARD)
                .justifyWith((lesson1, score) -> new GuideOnLevelJustification(lesson1))
                .asConstraint("Guide can do level");
    }

    Constraint UnAssignedGuide(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> lesson.getGuide() == null && ! lesson.getLessonType().equals("Admin Hold"))
                .penalize(HardSoftScore.ONE_HARD, lesson -> lesson.getStudentCount())
                .asConstraint("No Unassigned Guide for client session");
    }

}
