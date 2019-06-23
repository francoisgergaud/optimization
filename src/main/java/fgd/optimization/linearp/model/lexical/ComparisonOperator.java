package fgd.optimization.linearp.model.lexical;

/**
 * Comparison operator for linear-expression.
 */
public enum ComparisonOperator {

    EQ("="), GTE(">="), LTE("<=");

    public final String label;

    ComparisonOperator(String label) {
        this.label = label;
    }
}
