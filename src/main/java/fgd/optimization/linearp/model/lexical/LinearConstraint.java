package fgd.optimization.linearp.model.lexical;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a linear-constraint, which is a linear-expression lower-bounded or upper-bounded by a value.
 */
@AllArgsConstructor
@Getter
public class LinearConstraint {

    /**
     * The linear-expression.
     */
    private LinearExpression linearExpression;

    /**
     * The comparison-operator
     */
    private ComparisonOperator comparisonOperator;

    /**
     * The bound value.
     */
    private Double boundValue;
}
