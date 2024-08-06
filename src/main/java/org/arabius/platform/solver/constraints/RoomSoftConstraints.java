package org.arabius.platform.solver.constraints;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.solver.justifications.BigOnlineRoomJustification;
import org.arabius.platform.solver.justifications.BigRoomJustification;
import org.arabius.platform.solver.justifications.SameDaySameRoomJustification;
import org.arabius.platform.solver.justifications.SameRoomJustification;
import org.arabius.platform.solver.justifications.UnassignedRoomJustification;
import org.arabius.platform.solver.justifications.WrongBranchRoomJustification;
import org.arabius.platform.solver.justifications.RoomPriorityJustification;

import ai.timefold.solver.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class RoomSoftConstraints extends ArabiusConstraints {

        private Constraint onlineRoomInCorrectBranch(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEach(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson)
                                                && lesson.getRoom().getBranchId() != lesson.getBranchId()
                                                && lesson.isAllowVideoCall())
                                .penalize(HardSoftBigDecimalScore.ONE_SOFT, lesson -> 1000)
                                .justifyWith((lesson1, score) -> new WrongBranchRoomJustification(lesson1))
                                .asConstraint("No online lessons assigned to rooms in wrong branch");
        }

        private Constraint roomCapacityGreaterThanStudentCount(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEach(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson)
                                                && lesson.getRoom().getCapacity() > lesson.getStudentCount()
                                                && lesson.isEnforceRoomCapacity())
                                .penalize(HardSoftBigDecimalScore.ONE_SOFT,
                                                lesson -> lesson.getRoom().getCapacity() - lesson.getStudentCount())
                                .justifyWith((lesson1, score) -> new BigRoomJustification(lesson1))
                                .asConstraint("Room capacity greater than student count");
        }

        private Constraint roomCapacityGreaterThanOne(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEach(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson)
                                                && lesson.getRoom().getCapacity() > 1
                                                && !lesson.isEnforceRoomCapacity())
                                .penalize(HardSoftBigDecimalScore.ONE_SOFT,
                                                lesson -> lesson.getRoom().getCapacity() * 2)
                                .justifyWith((lesson1, score) -> new BigOnlineRoomJustification(lesson1))
                                .asConstraint("Room capacity greater than two for online lessons");
        }

        private Constraint useRoomPriorityByLessonType(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEach(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson) && lesson.getRoom() != null)
                                .penalize(HardSoftBigDecimalScore.ONE_SOFT,
                                                lesson -> lesson.getRoomPriorityForLessonType())
                                .justifyWith((lesson1, score) -> new RoomPriorityJustification(lesson1))
                                .asConstraint("Use room priority by lesson type");
        }

        private Constraint unAssignedOptionalRoom(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEachIncludingUnassigned(Lesson.class)
                                .filter((lesson) -> isLessonScheduled(lesson) && lesson.isAllowRoom()
                                                && !lesson.isRequireRoom()
                                                && lesson.getRoom() == null)
                                .penalize(HardSoftBigDecimalScore.ONE_SOFT, lesson -> 500)
                                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                                .asConstraint("No Unassigned optional room for client session");
        }

        private Constraint consecutiveLessonsInSameRoom(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEachUniquePair(Lesson.class,
                                                Joiners.equal(Lesson::getStudentGroupHash), // same student group
                                                Joiners.lessThan(Lesson::getBufferStart))
                                .filter((lesson1, lesson2) -> areBothLessonsScheduled(lesson1, lesson2)
                                                && !lesson1.getRoom().equals(lesson2.getRoom())
                                                && !lesson1.isAllowVideoCall()
                                                && !lesson2.isAllowVideoCall()
                                                && lesson1.isRequireRoom()
                                                && lesson2.isRequireRoom())
                                .penalize(HardSoftBigDecimalScore.ONE_SOFT,
                                                (lesson1, lesson2) -> lesson1.getStudentCount())
                                .justifyWith((lesson1, lesson2, score) -> new SameRoomJustification(lesson1, lesson2))
                                .asConstraint("Consecutive in-person lessons for the same clients should be in the same room");
        }

        private Constraint consecutiveSameDayLessonsInSameRoom(ConstraintFactory constraintFactory) {
                return constraintFactory
                                .forEachUniquePair(Lesson.class,
                                                Joiners.equal(Lesson::getStudentGroupHash), // same student group
                                                Joiners.lessThan(Lesson::getBufferStart),
                                                Joiners.equal(Lesson::getDate))
                                .filter((lesson1, lesson2) -> areBothLessonsScheduled(lesson1, lesson2)
                                                && !lesson1.getRoom().equals(lesson2.getRoom())
                                                && !lesson1.isAllowVideoCall()
                                                && !lesson2.isAllowVideoCall()
                                                && lesson1.isRequireRoom()
                                                && lesson2.isRequireRoom())
                                .penalize(HardSoftBigDecimalScore.ONE_SOFT, (lesson1, lesson2) -> 50)
                                .justifyWith((lesson1, lesson2, score) -> new SameDaySameRoomJustification(lesson1,
                                                lesson2))
                                .asConstraint("Consecutive Same-day in-person lessons for the same clients should be in the same room");
        }

        @Override
        public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
                return new Constraint[] { // 6180
                                onlineRoomInCorrectBranch(constraintFactory), // 5906
                                roomCapacityGreaterThanStudentCount(constraintFactory), // 6248
                                roomCapacityGreaterThanOne(constraintFactory), // 6109
                                useRoomPriorityByLessonType(constraintFactory), // 6116
                                consecutiveLessonsInSameRoom(constraintFactory), // 6457
                                consecutiveSameDayLessonsInSameRoom(constraintFactory), // 6266
                                unAssignedOptionalRoom(constraintFactory) // 6247
                };
        }
}
