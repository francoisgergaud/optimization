package fgd.optimization.linearp.model;

import fgd.optimization.linearp.model.lexical.ComparisonOperator;
import fgd.optimization.linearp.model.lexical.LinearConstraint;
import fgd.optimization.linearp.model.lexical.LinearExpression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

/**
 * A linear-constraint converted to an equality using a slacked variable. Using the rearrange method, the slack-variable
 * will be replaced by user-variable during the Simplex pivot operation.
 */
@AllArgsConstructor
@Getter
@ToString
public class SlackedLinearConstraint {

    /**
     * The linear-expression.
     */
    private LinearExpression linearExpression;

    /**
     * The value.
     */
    private Double value;

    /**
     * The variable.
     */
    private Variable variable;

    /**
     * example: slack-variable = bound-value - 3*variable should return (bound-value/3)
     *
     * @param variable       The variable to get the maximum value for.
     * @param floorToInteger Does the variable value has to be an integer (true) or a decimal (false)
     * @return The maximum value the variable can take without breaking the non-negative constraint
     */
    public Double maximumValue(Variable variable, boolean floorToInteger) {
        Double result = value / linearExpression.getCoefficientByVariable().get(variable);
        if (floorToInteger) {
            result = Math.floor(result);
        }
        return result;
    }

    /**
     * Flip the input-variable in the variable part and rearrange the expression so that each coefficient is scaled to
     * the variable factor and the right-side contains the previous left-side variable.
     * example: c = 12 - 2a + 3b, with input-variable = a => a = 6 + (3/2)b - (1/2) c
     *
     * @param variable The varibale to put on the left-side.
     */
    public void rearrange(Variable variable) {
        // get the variable coefficient
        Double coefficient = linearExpression.getCoefficientByVariable().get(variable);
        // remove the variable from the right-side
        linearExpression.getCoefficientByVariable().remove(variable);
        // move the left-side variable to the right-side
        linearExpression.getCoefficientByVariable().put(this.variable, 1d);
        // divide all the left-side coefficient by the one from the variable being moved to the left-side
        for (Variable linearExpressionVar : linearExpression.getCoefficientByVariable().keySet()) {
            linearExpression.getCoefficientByVariable().put(
                    linearExpressionVar,
                    linearExpression.getCoefficientByVariable().get(linearExpressionVar) / coefficient
            );
        }
        // move the variable to the left-side
        this.variable = variable;
        this.value = this.value / coefficient;
    }

    /**
     * Replace a variable by an linear-expression of other variables.
     * example: expression : c = 3a + b and a = 2b + 4c => c = 7b + 12c.
     * Note: In theory, left-side variable does not have to be changed, as it should not be part of the linear-expression.
     * Simplex works in a way that at a given time,  a user-variable is on the left-side in only one constraint, and
     * absent of all other constraint right-side.
     *
     * @param replacement The expression to use as a substitute, with the variable to be substituted.
     */
    public void applyGaussJordanElimination(SlackedLinearConstraint replacement) {
        // get the variable coefficient
        Double coefficient = this.linearExpression.getCoefficientByVariable().get(replacement.variable);
        // remove the variable from the right-side
        this.linearExpression.getCoefficientByVariable().remove(replacement.variable);
        for (Map.Entry<Variable, Double> entry : replacement.linearExpression.getCoefficientByVariable().entrySet()) {
            Double newValue = -(entry.getValue() * coefficient);
            if (this.linearExpression.getCoefficientByVariable().containsKey(entry.getKey())) {
                newValue += this.linearExpression.getCoefficientByVariable().get(entry.getKey());
            }
            this.linearExpression.getCoefficientByVariable().put(entry.getKey(), newValue);
        }
        this.value -= coefficient * replacement.value;
    }

}
