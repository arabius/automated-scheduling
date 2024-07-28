package org.arabius.platform.solver;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.arabius.platform.solver.constraints.GuideHardConstraints;
import org.arabius.platform.solver.constraints.GuideSoftConstraints;
import org.arabius.platform.solver.constraints.RoomHardConstraints;
import org.arabius.platform.solver.constraints.RoomSoftConstraints;


public class TimetableConstraintProvider implements ConstraintProvider {

        public TimetableConstraintProvider() {
        }

        @Override
        public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
                List<Constraint> constraints = new ArrayList<>();
                constraints.addAll(Arrays.asList(new RoomHardConstraints().getConstraints(constraintFactory)));
                constraints.addAll(Arrays.asList(new RoomSoftConstraints().getConstraints(constraintFactory)));
                constraints.addAll(Arrays.asList(new GuideHardConstraints().getConstraints(constraintFactory)));
                constraints.addAll(Arrays.asList(new GuideSoftConstraints().getConstraints(constraintFactory)));

               return constraints.toArray(new Constraint[0]);
        }
}
