package fgd.optimization.linearp.transform;

import fgd.optimization.linearp.model.SlackVariable;
import fgd.optimization.linearp.model.SlackedLinearConstraint;
import fgd.optimization.linearp.model.Variable;
import fgd.optimization.linearp.model.lexical.*;

public class ObjectiveFunctionToSlackedContraints {

    /**
     * TODO: set the value properly.
     * @param objectiveFunction The objective-function to convert.
     * @return The Objective-function converted.
     */
    public SlackedLinearConstraint transform(ObjectiveFunction objectiveFunction){
        Double value = 0d;
        if(objectiveFunction.getOptimizationOperator().equals(OptimizationOperator.MAX)) {
            //all coefficient are reverted => Z - objective-function = 0
            LinearExpression linearExpression = objectiveFunction.getLinearExpression();
            for (Variable variable : linearExpression.getCoefficientByVariable().keySet()) {
                linearExpression.getCoefficientByVariable().put(variable, -linearExpression.getCoefficientByVariable().get(variable));
            }
        }
        return new SlackedLinearConstraint(
            objectiveFunction.getLinearExpression(),
            value,
            new SlackVariable()
        );
    }
}
