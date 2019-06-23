package fgd.optimization.linearp;

import com.google.common.collect.ImmutableList;
import fgd.optimization.linearp.model.UserVariable;
import fgd.optimization.linearp.model.Variable;
import fgd.optimization.linearp.model.lexical.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SimplexTest {

    /**
     * MAXIMIZE Z = X1 + 2*X2 - X3
     * SUBJECT TO:
     *  2*X1 + X2 + X3 <= 14
     *  4*X1 + 2*X2 + 3*X3 <= 28
     *  2*X1 + 5*X2 + 5*X3 <= 30
     *  X1 >= 0
     *  X2 >= 0
     *  X3 >= 0
     *
     *  Must result in:
     *  S1 = 0, S2 = 0, S3 = 0, X3 = 0, X1 = 5, X2 = 4
     *  Z = 13
     *
     *  (http://math.uww.edu/~mcfarlat/s-prob.htm)
     */
    @Test
    public void nominal1Test(){
        //variables
        Variable x1 = new UserVariable("X1");
        Variable x2 = new UserVariable("X2");
        Variable x3 = new UserVariable("X3");

        //objective function
        LinearExpression objectiveFunctionLinearExp = new LinearExpression();
        objectiveFunctionLinearExp.addVariable(x1, 1d);
        objectiveFunctionLinearExp.addVariable(x2, 2d);
        objectiveFunctionLinearExp.addVariable(x3, -1d);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction(objectiveFunctionLinearExp, OptimizationOperator.MAX);
        //constraint 1
        LinearExpression constraint1LinearExp = new LinearExpression();
        constraint1LinearExp.addVariable(x1, 2d);
        constraint1LinearExp.addVariable(x2, 1d);
        constraint1LinearExp.addVariable(x3, 1d);
        LinearConstraint constraint1 = new LinearConstraint(constraint1LinearExp, ComparisonOperator.LTE, 14d);
        //constraint 2
        LinearExpression constraint2LinearExp = new LinearExpression();
        constraint2LinearExp.addVariable(x1, 4d);
        constraint2LinearExp.addVariable(x2, 2d);
        constraint2LinearExp.addVariable(x3, 3d);
        LinearConstraint constraint2 = new LinearConstraint(constraint2LinearExp, ComparisonOperator.LTE, 28d);
        //constraint 2
        LinearExpression constraint3LinearExp = new LinearExpression();
        constraint3LinearExp.addVariable(x1, 2d);
        constraint3LinearExp.addVariable(x2, 5d);
        constraint3LinearExp.addVariable(x3, 5d);
        LinearConstraint constraint3 = new LinearConstraint(constraint3LinearExp, ComparisonOperator.LTE, 30d);
        Simplex simplex = new Simplex(objectiveFunction, ImmutableList.of(constraint1, constraint2, constraint3), true);
        Solution solution = simplex.solve();
        Assert.assertEquals( 13d, solution.getZValue(), 0);
    }

    /**
     * MAXIMIZE Z = 3*X + 4*y
     * SUBJECT TO:
     *  x + y <= 4
     *  2*x + y <= 5
     *  x >= 0
     *  y >= 0
     *
     *  Must result in:
     *  S1 = 0, S2 = 1, x = 0, y = 4
     *  Z = 16
     *
     *  (http://www.ms.uky.edu/~rwalker/Class%20Work%20Solutions/class%20work%208%20solutions.pdf)
     */
    @Test
    public void nominal2Test(){
        //variables
        Variable x = new UserVariable("x");
        Variable y = new UserVariable("y");

        //objective function
        LinearExpression objectiveFunctionLinearExp = new LinearExpression();
        objectiveFunctionLinearExp.addVariable(x, 3d);
        objectiveFunctionLinearExp.addVariable(y, 4d);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction(objectiveFunctionLinearExp, OptimizationOperator.MAX);
        //constraint 1
        LinearExpression constraint1LinearExp = new LinearExpression();
        constraint1LinearExp.addVariable(x, 1d);
        constraint1LinearExp.addVariable(y, 1d);
        LinearConstraint constraint1 = new LinearConstraint(constraint1LinearExp, ComparisonOperator.LTE, 4d);
        //constraint 2
        LinearExpression constraint2LinearExp = new LinearExpression();
        constraint2LinearExp.addVariable(x, 2d);
        constraint2LinearExp.addVariable(y, 1d);
        LinearConstraint constraint2 = new LinearConstraint(constraint2LinearExp, ComparisonOperator.LTE, 5d);
        Simplex simplex = new Simplex(objectiveFunction, ImmutableList.of(constraint1, constraint2), true);
        Solution solution = simplex.solve();
        Assert.assertEquals( 16d, solution.getZValue(), 0);
    }

    /**
     * MAXIMIZE Z = 3*X1 + 2*X2
     * SUBJECT TO:
     *  -1*X1 + 2*X2 <= 4
     *  3*X1 + 2*X2 <= 14
     *  X1 + -1*X2 <= 3
     *  X1 >= 0
     *  X2 >= 0
     *  X3 >= 0
     *
     *  Must result in:
     *  X1 = 4, X2 = 1
     *  Z = 14
     *
     *  (http://www.universalteacherpublications.com/univ/ebooks/or/Ch3/simplex.htm)
     */
    @Test
    public void nominal3Test(){
        //variables
        Variable x1 = new UserVariable("X1");
        Variable x2 = new UserVariable("X2");

        //objective function
        LinearExpression objectiveFunctionLinearExp = new LinearExpression();
        objectiveFunctionLinearExp.addVariable(x1, 3d);
        objectiveFunctionLinearExp.addVariable(x2, 2d);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction(objectiveFunctionLinearExp, OptimizationOperator.MAX);
        //constraint 1
        LinearExpression constraint1LinearExp = new LinearExpression();
        constraint1LinearExp.addVariable(x1, -1d);
        constraint1LinearExp.addVariable(x2, 2d);
        LinearConstraint constraint1 = new LinearConstraint(constraint1LinearExp, ComparisonOperator.LTE, 4d);
        //constraint 2
        LinearExpression constraint2LinearExp = new LinearExpression();
        constraint2LinearExp.addVariable(x1, 3d);
        constraint2LinearExp.addVariable(x2, 2d);
        LinearConstraint constraint2 = new LinearConstraint(constraint2LinearExp, ComparisonOperator.LTE, 14d);
        //constraint 2
        LinearExpression constraint3LinearExp = new LinearExpression();
        constraint3LinearExp.addVariable(x1, 1d);
        constraint3LinearExp.addVariable(x2, -1d);
        LinearConstraint constraint3 = new LinearConstraint(constraint3LinearExp, ComparisonOperator.LTE, 3d);
        Simplex simplex = new Simplex(objectiveFunction, ImmutableList.of(constraint1, constraint2, constraint3), true);
        Solution solution = simplex.solve();
        Assert.assertEquals( 14d, solution.getZValue(), 0);
    }

    /**
     * MAXIMIZE Z = X1 + X2 + 2*X3
     * SUBJECT TO:
     *  2*X1 + X2 + X3 <= 50
     *  2*X1 + X2 <= 36
     *  X1 + -1*X2 <= 3
     *  X1 >= 0
     *  X2 >= 0
     *  X3 >= 0
     *
     *  Must result in:
     *  X1 = 4, X2 = 1
     *  Z = 14
     *
     *  (http://college.cengage.com/mathematics/larson/elementary_linear/4e/shared/downloads/c09s5.pdf)
     */
    @Test
    public void nominal4Test(){
        //variables
        Variable x1 = new UserVariable("X1");
        Variable x2 = new UserVariable("X2");

        //objective function
        LinearExpression objectiveFunctionLinearExp = new LinearExpression();
        objectiveFunctionLinearExp.addVariable(x1, 3d);
        objectiveFunctionLinearExp.addVariable(x2, 2d);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction(objectiveFunctionLinearExp, OptimizationOperator.MAX);
        //constraint 1
        LinearExpression constraint1LinearExp = new LinearExpression();
        constraint1LinearExp.addVariable(x1, -1d);
        constraint1LinearExp.addVariable(x2, 2d);
        LinearConstraint constraint1 = new LinearConstraint(constraint1LinearExp, ComparisonOperator.LTE, 4d);
        //constraint 2
        LinearExpression constraint2LinearExp = new LinearExpression();
        constraint2LinearExp.addVariable(x1, 3d);
        constraint2LinearExp.addVariable(x2, 2d);
        LinearConstraint constraint2 = new LinearConstraint(constraint2LinearExp, ComparisonOperator.LTE, 14d);
        //constraint 2
        LinearExpression constraint3LinearExp = new LinearExpression();
        constraint3LinearExp.addVariable(x1, 1d);
        constraint3LinearExp.addVariable(x2, -1d);
        LinearConstraint constraint3 = new LinearConstraint(constraint3LinearExp, ComparisonOperator.LTE, 3d);
        Simplex simplex = new Simplex(objectiveFunction, ImmutableList.of(constraint1, constraint2, constraint3), true);
        Solution solution = simplex.solve();
        Assert.assertEquals( 14d, solution.getZValue(), 0);
    }
}
