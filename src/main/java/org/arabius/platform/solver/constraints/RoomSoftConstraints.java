package org.arabius.platform.solver.constraints;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.solver.justifications.BigOnlineRoomJustification;
import org.arabius.platform.solver.justifications.BigRoomJustification;
import org.arabius.platform.solver.justifications.SameDaySameRoomJustification;
import org.arabius.platform.solver.justifications.SameRoomJustification;
import org.arabius.platform.solver.justifications.UnassignedRoomJustification;
import org.arabius.platform.solver.justifications.WrongBranchRoomJustification;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class RoomSoftConstraints extends ArabiusConstraints {
    
    private Constraint onlineRoomInCorrectBranch(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) && lesson.getRoom().getBranch() != lesson.getBranchId() && lesson.getLessonType().isAllowVideoCall())
                .penalize(HardSoftScore.ONE_SOFT, lesson -> 1000)
                .justifyWith((lesson1, score) -> new WrongBranchRoomJustification(lesson1))
                .asConstraint("No online lessons assigned to rooms in wrong branch");
    }

//     private Constraint UnAssignedRoomAdmin(ConstraintFactory constraintFactory) {
//         return constraintFactory
//                 .forEachIncludingUnassigned(Lesson.class)
//                 .filter((lesson) -> lesson.getRoom() == null && lesson.getLessonType().equals("Admin Hold"))
//                 .penalize(HardSoftScore.ONE_SOFT)
//                 .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
//                 .asConstraint("Unassigned room for admin hold session");
//     }

    private Constraint roomCapacityGreaterThanStudentCount(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) && lesson.getRoom().getCapacity() > lesson.getStudentCount() && lesson.getLessonType().isEnforceRoomCapacity())
                .penalize(HardSoftScore.ONE_SOFT, lesson -> lesson.getRoom().getCapacity() - lesson.getStudentCount())
                .justifyWith((lesson1, score) -> new BigRoomJustification(lesson1))
                .asConstraint("Room capacity greater than student count");
    }

    private Constraint roomCapacityGreaterThanOne(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) 
                    && lesson.getRoom().getCapacity() > 1
                    && ! lesson.getLessonType().isEnforceRoomCapacity())
                .penalize(HardSoftScore.ONE_SOFT, lesson -> lesson.getRoom().getCapacity()*2)
                .justifyWith((lesson1, score) -> new BigOnlineRoomJustification(lesson1))
                .asConstraint("Room capacity greater than two for online lessons");
    }

    private Constraint useRoomPriorityByLessonType(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson))
                .penalize(HardSoftScore.ONE_SOFT, lesson -> (int) lesson.getRoom().getRoomPriorityForLessonType(lesson.getLessonType().getId()))
                .asConstraint("Use room priority by lesson type");
    }

    private Constraint unAssignedOptionalRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) &&  lesson.getLessonType().getRoomRequirment().isOptional() && lesson.getRoom() == null)
                .penalize(HardSoftScore.ONE_SOFT)
                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                .asConstraint("No Unassigned optional room for client session");
    }

    private Constraint consecutiveLessonsInSameRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.lessThan(Lesson::getBufferStart))
                .filter((lesson1, lesson2) -> lessonsAreInFuture(lesson1, lesson2) && !lesson1.getRoom().equals(lesson2.getRoom()) && ! lesson1.getLessonType().isAllowVideoCall() && ! lesson2.getLessonType().isAllowVideoCall())
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> lesson1.getStudentCount())
                .justifyWith((lesson1, lesson2, score) -> new SameRoomJustification(lesson1, lesson2))
                .asConstraint("Consecutive in-person lessons for the same clients should be in the same room");
    }

    private Constraint consecutiveSameDayLessonsInSameRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroupHash), // same student group
                        Joiners.lessThan(Lesson::getBufferStart),
                        Joiners.equal(Lesson::getDate)) 
                .filter((lesson1, lesson2) -> lessonsAreInFuture(lesson1, lesson2) && !lesson1.getRoom().equals(lesson2.getRoom()) && lesson1.getLessonType().equals("In-person") && lesson2.getLessonType().equals("In-person"))
                .penalize(HardSoftScore.ONE_SOFT, (lesson1, lesson2) -> 50)
                .justifyWith((lesson1, lesson2, score) -> new SameDaySameRoomJustification(lesson1, lesson2))
                .asConstraint("Consecutive Same-day in-person lessons for the same clients should be in the same room");
    }

    @Override
    public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                onlineRoomInCorrectBranch(constraintFactory),
                roomCapacityGreaterThanStudentCount(constraintFactory),
                roomCapacityGreaterThanOne(constraintFactory),
                useRoomPriorityByLessonType(constraintFactory),
                consecutiveLessonsInSameRoom(constraintFactory),
                consecutiveSameDayLessonsInSameRoom(constraintFactory),
                unAssignedOptionalRoom(constraintFactory)
        };
    }
}
