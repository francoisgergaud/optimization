package fgd.optimization.linearp.model.lexical;

import fgd.optimization.linearp.model.SlackedLinearConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/**
 * TODO: rework the models as Objective function can use some part of the SlackedLinearConstraint class (bound-value,
 * linear-expression and replace method). It will add a evaluate method specific for objective-function.
 */
public class ObjectiveFunction {

    private LinearExpression linearExpression;
    private OptimizationOperator optimizationOperator;
}
