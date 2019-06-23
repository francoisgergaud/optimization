package fgd.optimization.linearp;

import fgd.optimization.linearp.model.SlackedLinearConstraint;
import fgd.optimization.linearp.model.Variable;
import fgd.optimization.linearp.model.lexical.LinearConstraint;
import fgd.optimization.linearp.model.lexical.ObjectiveFunction;
import fgd.optimization.linearp.model.lexical.Solution;
import fgd.optimization.linearp.transform.ConstraintsToSlackedContraints;
import fgd.optimization.linearp.transform.ObjectiveFunctionToSlackedContraints;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class Simplex {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simplex.class);

    private ObjectiveFunction objectiveFunction;
    private List<LinearConstraint> constraints;
    private boolean floorToInteger = true;

    public Solution solve(){
        ConstraintsToSlackedContraints constraintConverter = new ConstraintsToSlackedContraints();
        List<SlackedLinearConstraint> slackedConstraints = constraintConverter.transform(constraints);
        ObjectiveFunctionToSlackedContraints objectiveFunctionConverter = new ObjectiveFunctionToSlackedContraints();
        SlackedLinearConstraint slackedObjectiveFunction = objectiveFunctionConverter.transform(objectiveFunction);
        Map<Variable, Double> result = findFeasibleSolution(slackedConstraints);
        Variable enteringVariable;
        LOGGER.trace("Feasible solution: Z={}, variables={}", slackedObjectiveFunction.getValue(), result);
        while ((enteringVariable = pickMostNegativeCoefficientFromObjectiveFunction(slackedObjectiveFunction)) != null) {
            SlackedLinearConstraint constraint = getMostRestrictiveConstraintForVariable(enteringVariable, slackedConstraints);
            LOGGER.trace("Entering variable: {}, departing variable: {}", enteringVariable.getName(), constraint.getVariable().getName());
            pivot(enteringVariable, constraint, slackedConstraints, slackedObjectiveFunction, result);
            LOGGER.trace("Current solution: Z={}, variables={}", slackedObjectiveFunction.getValue(), result);
        }
        return new Solution(slackedObjectiveFunction.getValue(), result);
    }

    /**
     * TODO: improve the feasible solution: give an array of all variable-names in input
     * @param slackedConstraints The list of slacked-contraint to find a feasible solution for.
     * @return A feasible solution.
     */
    private Map<Variable, Double> findFeasibleSolution(List<SlackedLinearConstraint> slackedConstraints){
        Map<Variable, Double> result = new HashMap<>();
        for(SlackedLinearConstraint slackedLinearConstraint : slackedConstraints){
            for(Variable variable: slackedLinearConstraint.getLinearExpression().getCoefficientByVariable().keySet()){
                result.put(variable, 0d);
            }
            result.put(slackedLinearConstraint.getVariable(), slackedLinearConstraint.getValue());
        }
        return result;
    }

    /**
     * Pick a variable which has a most negative coefficient in the object-function.
     * @param objectiveFunction The object-function.
     * @return A variable with the most negative coefficient, or null is there is not.
     */
    private Variable pickMostNegativeCoefficientFromObjectiveFunction(SlackedLinearConstraint objectiveFunction){
        Double currentMinimum = 0d;
        Variable currentVariable = null;
        for(Map.Entry<Variable, Double> coefficientByVariable : objectiveFunction.getLinearExpression().getCoefficientByVariable().entrySet()){
            if(coefficientByVariable.getValue() < currentMinimum){
                currentMinimum = coefficientByVariable.getValue();
                currentVariable = coefficientByVariable.getKey();
            }
        }
        return currentVariable;
    }

    /**
     * find the most restrictive constraint for a variable.
     * @param variable The variable to get the most restrictive constraint for.
     * @param slackedConstraints The list of all constraints.
     * @return The most restrictive constraint.
     */
    private SlackedLinearConstraint getMostRestrictiveConstraintForVariable(Variable variable, List<SlackedLinearConstraint> slackedConstraints){
        SlackedLinearConstraint result = null;
        Double currentMinimumValue = null;
        for(SlackedLinearConstraint slackedLinearConstraint : slackedConstraints){
            Double constraintMaximumValue = slackedLinearConstraint.maximumValue(variable, this.floorToInteger);
            if(constraintMaximumValue > 0 && (currentMinimumValue == null || constraintMaximumValue < currentMinimumValue)){
                currentMinimumValue = constraintMaximumValue;
                result = slackedLinearConstraint;
            }
        }
        return result;
    }

    /**
     * Apply the pivot operation on the Simplex table (made of slacked constraints and an objective-function).
     * @param enteringVariable The entering variable.
     * @param mostRestrictiveConstraint The most restrictive constraint (with the departing variable).
     * @param slackedConstraints The slacked-constraints (lines ot the Simplex table).
     * @param slackedObjectiveFunction The objective-function (bottom line of the Simplex table).
     * @param result The result (list of all variables with their values).
     */
    private void pivot(Variable enteringVariable,
                       SlackedLinearConstraint mostRestrictiveConstraint,
                       List<SlackedLinearConstraint> slackedConstraints,
                       SlackedLinearConstraint slackedObjectiveFunction,
                       Map<Variable, Double> result){
        result.put(mostRestrictiveConstraint.getVariable(), 0d);
        mostRestrictiveConstraint.rearrange(enteringVariable);
        result.put(enteringVariable, mostRestrictiveConstraint.getValue());
        LOGGER.trace("pivoting...");
        for(SlackedLinearConstraint slackedLinearConstraint : slackedConstraints){
            if(slackedLinearConstraint != mostRestrictiveConstraint){
                slackedLinearConstraint.applyGaussJordanElimination(mostRestrictiveConstraint);
                result.put(slackedLinearConstraint.getVariable(), slackedLinearConstraint.getValue());
            }
            LOGGER.trace("constraint: {}", slackedLinearConstraint);
        }
        slackedObjectiveFunction.applyGaussJordanElimination(mostRestrictiveConstraint);
        LOGGER.trace("objective-function: {}", slackedObjectiveFunction);

    }
}
