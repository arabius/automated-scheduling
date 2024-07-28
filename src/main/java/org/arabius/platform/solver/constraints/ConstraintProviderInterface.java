package org.arabius.platform.solver.constraints;


import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;

public interface ConstraintProviderInterface {
    Constraint[] getConstraints(ConstraintFactory constraintFactory);
}