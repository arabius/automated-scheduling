package org.arabius.platform.solver.constraints;

import java.util.Arrays;
import java.util.List;

import org.arabius.platform.domain.Lesson;
import org.arabius.platform.solver.justifications.RoomConflictJustification;
import org.arabius.platform.solver.justifications.SmallRoomJustification;
import org.arabius.platform.solver.justifications.UnassignedRoomJustification;
import org.arabius.platform.solver.justifications.WrongBranchRoomJustification;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class RoomHardConstraints extends ArabiusConstraints {

    public Constraint roomConflict(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                // Select each pair of 2 different lessons ...
                .forEachUniquePair(Lesson.class,
                        // ... overlapping in time ...
                        Joiners.overlapping(Lesson::getBufferStart, Lesson::getBufferEnd),
                        // ... in the same room ...
                        Joiners.equal(Lesson::getRoom))
                // ... and penalize each pair with a hard weight.
                .filter((lesson1, lesson2) -> lessonsAreInFuture(lesson1, lesson2))
                .penalize(HardSoftScore.ONE_HARD, (lesson1, lesson2) -> 100)
                .justifyWith((lesson1, lesson2, score) -> new RoomConflictJustification(lesson1.getRoom(), lesson1, lesson2))
                .asConstraint("Room conflict");
    }

    private Constraint unAssignedRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) 
                    && lesson.getLessonType().getRoomRequirment().isRequired()
                    && lesson.getRoom() == null)
                .penalize(HardSoftScore.ONE_HARD, lesson -> lesson.getStudentCount())
                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                .asConstraint("No Unassigned room for client session");
    }

    private Constraint assignedRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) 
                    && ! lesson.getLessonType().getRoomRequirment().isCanAssign()
                    && lesson.getRoom() != null)
                .penalize(HardSoftScore.ONE_HARD, lesson -> 1000)
                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                .asConstraint("No Assigned rooms for not allowed lesson types");
    }

    private Constraint inpersonRoomInCorrectBranch(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> lessonIsInFuture(lesson) && lesson.getRoom().getBranch() != lesson.getBranchId() && ! lesson.getLessonType().isAllowVideoCall())
                .penalize(HardSoftScore.ONE_HARD, lesson -> 100)
                .justifyWith((lesson1, score) -> new WrongBranchRoomJustification(lesson1))
                .asConstraint("No in-person lessons assigned to rooms in wrong branch");
    }

    private Constraint roomCapacityLessThanStudentCount(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter(lesson -> lessonIsInFuture(lesson) 
                    && lesson.getRoom().getCapacity() < lesson.getStudentCount() 
                    && lesson.getLessonType().isEnforceRoomCapacity())
                .penalize(HardSoftScore.ONE_HARD, lesson-> (lesson.getRoom().getCapacity() - lesson.getStudentCount()) * 100)
                .justifyWith((lesson1, score) -> new SmallRoomJustification(lesson1))
                .asConstraint("Room capacity less than student count");
    }

    @Override
    public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                roomConflict(constraintFactory),
                unAssignedRoom(constraintFactory),
                assignedRoom(constraintFactory),
                inpersonRoomInCorrectBranch(constraintFactory),
                roomCapacityLessThanStudentCount(constraintFactory),
                
        };
    }

    
}
