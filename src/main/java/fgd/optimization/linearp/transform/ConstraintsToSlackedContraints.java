package fgd.optimization.linearp.transform;

import fgd.optimization.linearp.model.SlackVariable;
import fgd.optimization.linearp.model.SlackedLinearConstraint;
import fgd.optimization.linearp.model.Variable;
import fgd.optimization.linearp.model.lexical.ComparisonOperator;
import fgd.optimization.linearp.model.lexical.LinearConstraint;
import fgd.optimization.linearp.model.lexical.LinearExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Constraint to slacked-constraint transformer.
 */
public class ConstraintsToSlackedContraints {

    public List<SlackedLinearConstraint> transform(List<LinearConstraint> contraints){
        List<SlackedLinearConstraint> result = new ArrayList<SlackedLinearConstraint>(contraints.size());
        for(LinearConstraint linearConstraint: contraints){
            if(linearConstraint.getComparisonOperator().equals(ComparisonOperator.LTE)){
                 LinearExpression linearExpression = linearConstraint.getLinearExpression();
                 SlackedLinearConstraint slackedLinearConstraint = new SlackedLinearConstraint(
                        linearExpression,
                        linearConstraint.getBoundValue(),
                        new SlackVariable());
                result.add(slackedLinearConstraint);
            }else if(linearConstraint.getComparisonOperator().equals(ComparisonOperator.GTE)){
                // a + b >= bound-value becomes slack-variable = (a + b) - bound-value
                LinearExpression linearExpression = linearConstraint.getLinearExpression();
                for(Variable variable : linearExpression.getCoefficientByVariable().keySet()){
                    linearExpression.getCoefficientByVariable().put(variable, -linearExpression.getCoefficientByVariable().get(variable));
                }

                SlackedLinearConstraint slackedLinearConstraint = new SlackedLinearConstraint(
                        linearConstraint.getLinearExpression(),
                        -linearConstraint.getBoundValue(),
                        new SlackVariable());
                result.add(slackedLinearConstraint);
            }
        }
        return result;
    }
}
