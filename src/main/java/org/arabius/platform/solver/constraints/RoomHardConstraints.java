package org.arabius.platform.solver.constraints;

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
                        // ... in the same room ...
                        Joiners.equal(Lesson::getRoom),
                        // ... on the same day ...
                        Joiners.equal(Lesson::getDate),
                        // ... that overlap each other ...
                        Joiners.overlapping(Lesson::getBufferStart, Lesson::getBufferEnd)
                        )
                // ... and penalize each pair with a hard weight.
                .filter((lesson1, lesson2) -> areBothLessonsScheduled(lesson1, lesson2))
                .penalize(HardSoftScore.ONE_HARD, (lesson1, lesson2) -> 1000)
                .justifyWith((lesson1, lesson2, score) -> new RoomConflictJustification(lesson1.getRoom(), lesson1, lesson2))
                .asConstraint("Room conflict");
    }

    private Constraint unAssignedRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> isLessonScheduled(lesson) 
                    && lesson.isRequireRoom()
                    && lesson.getRoom() == null)
                .penalize(HardSoftScore.ONE_HARD, lesson -> lesson.getStudentCount())
                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                .asConstraint("No Unassigned room for client session");
    }

    private Constraint assignedRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingUnassigned(Lesson.class)
                .filter((lesson) -> isLessonScheduled(lesson) 
                    && ! lesson.isAllowRoom()
                    && lesson.getRoom() != null)
                .penalize(HardSoftScore.ONE_HARD, lesson -> 1000)
                .justifyWith((lesson1, score) -> new UnassignedRoomJustification(lesson1))
                .asConstraint("No Assigned rooms for not allowed lesson types");
    }

    private Constraint inpersonRoomInCorrectBranch(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter((lesson) -> isLessonScheduled(lesson) && lesson.getRoom().getBranchId() != lesson.getBranchId() && ! lesson.isAllowVideoCall())
                .penalize(HardSoftScore.ONE_HARD, lesson -> 50)
                .justifyWith((lesson1, score) -> new WrongBranchRoomJustification(lesson1))
                .asConstraint("No in-person lessons assigned to rooms in wrong branch");
    }

    private Constraint roomCapacityLessThanStudentCount(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter(lesson -> isLessonScheduled(lesson) 
                    && lesson.getRoom().getCapacity() < lesson.getStudentCount() 
                    && lesson.isEnforceRoomCapacity())
                .penalize(HardSoftScore.ONE_HARD, lesson-> (lesson.getRoom().getCapacity() - lesson.getStudentCount()) * 10)
                .justifyWith((lesson1, score) -> new SmallRoomJustification(lesson1))
                .asConstraint("Room capacity less than student count");
    }

    @Override
    public Constraint[] getConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                roomConflict(constraintFactory), //301550
                unAssignedRoom(constraintFactory), //7221
                assignedRoom(constraintFactory), //7041
                inpersonRoomInCorrectBranch(constraintFactory), //7023
                roomCapacityLessThanStudentCount(constraintFactory), //7082
                
        };
    }

    
}
