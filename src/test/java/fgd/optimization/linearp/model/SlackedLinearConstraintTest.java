package fgd.optimization.linearp.model;

import fgd.optimization.linearp.model.lexical.LinearExpression;
import org.junit.Assert;
import org.junit.Test;

public class SlackedLinearConstraintTest {

    @Test
    public void maximumValueTest() {
        UserVariable a = new UserVariable("a");
        UserVariable b = new UserVariable("b");
        UserVariable c = new UserVariable("c");
        LinearExpression linearExpression = new LinearExpression();
        linearExpression.addVariable(b, 2d);
        linearExpression.addVariable(c, 3d);
        Double value = 6d;
        SlackedLinearConstraint constraint = new SlackedLinearConstraint(linearExpression, value, a);
        Assert.assertEquals(3d, constraint.maximumValue(b, true), 0);
    }

    @Test
    public void reaarangeTest() {
        UserVariable a = new UserVariable("a");
        UserVariable b = new UserVariable("b");
        UserVariable c = new UserVariable("c");
        LinearExpression linearExpression = new LinearExpression();
        linearExpression.addVariable(b, 2d);
        linearExpression.addVariable(c, 3d);
        Double value = 6d;
        SlackedLinearConstraint constraint = new SlackedLinearConstraint(linearExpression, value, a);

        constraint.rearrange(b);

        Assert.assertEquals(b, constraint.getVariable());
        Assert.assertEquals(6d, constraint.getValue(), 0);
        Assert.assertTrue(constraint.getLinearExpression().getCoefficientByVariable().containsKey(a));
        Assert.assertEquals(0.5d, constraint.getLinearExpression().getCoefficientByVariable().get(a), 0);
        Assert.assertTrue(constraint.getLinearExpression().getCoefficientByVariable().containsKey(c));
        Assert.assertEquals(1.5d, constraint.getLinearExpression().getCoefficientByVariable().get(c), 0);
        Assert.assertFalse(constraint.getLinearExpression().getCoefficientByVariable().containsKey(b));
        Assert.assertEquals(2, constraint.getLinearExpression().getCoefficientByVariable().keySet().size());
    }

    @Test
    public void GaussianJordanElimination() {
        UserVariable a = new UserVariable("a");
        UserVariable b = new UserVariable("b");
        UserVariable c = new UserVariable("c");
        LinearExpression linearExpression = new LinearExpression();
        linearExpression.addVariable(b, 2d);
        linearExpression.addVariable(c, 3d);
        Double value = 6d;
        SlackedLinearConstraint constraint = new SlackedLinearConstraint(linearExpression, value, a);

        UserVariable d = new UserVariable("d");
        LinearExpression replacementLinearExpression = new LinearExpression();
        replacementLinearExpression.addVariable(d, 2d);
        replacementLinearExpression.addVariable(c, 3d);
        Double replacementValue = 5d;
        SlackedLinearConstraint replacement = new SlackedLinearConstraint(replacementLinearExpression, replacementValue, b);

        constraint.applyGaussJordanElimination(replacement);

        Assert.assertEquals(a, constraint.getVariable());
        Assert.assertEquals(16d, constraint.getValue(), 0);
        Assert.assertTrue(constraint.getLinearExpression().getCoefficientByVariable().containsKey(c));
        Assert.assertEquals(-3d, constraint.getLinearExpression().getCoefficientByVariable().get(c), 0);
        Assert.assertTrue(constraint.getLinearExpression().getCoefficientByVariable().containsKey(d));
        Assert.assertEquals(-4d, constraint.getLinearExpression().getCoefficientByVariable().get(d), 0);
        Assert.assertFalse(constraint.getLinearExpression().getCoefficientByVariable().containsKey(b));
        Assert.assertEquals(2, constraint.getLinearExpression().getCoefficientByVariable().keySet().size());
    }

}
