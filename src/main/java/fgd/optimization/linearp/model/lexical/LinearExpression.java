package fgd.optimization.linearp.model.lexical;

import fgd.optimization.linearp.model.Variable;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a linear-expression, which is a sum of VariableCoefficients.
 */
@ToString
public class LinearExpression {

    @Getter
    private Map<Variable,Double> coefficientByVariable;

    public LinearExpression(){
        this.coefficientByVariable = new HashMap<>();
    }



    public Double evaluate(Map<Variable, Double> variablesWithValue){
        Double result = 0d;
        for(Variable variableName : coefficientByVariable.keySet()){
            if(variablesWithValue.containsKey(variableName))
                result += coefficientByVariable.get(variableName) * variablesWithValue.get(variableName);
        }
        return result;
    }

    /**
     * Add a variable with its coeffcient into the linear expression.
     * @param variable The variable to add.
     * @param coefficient The variable's coefficient.
     * @return The updated linear-expression.
     */
    public Map<Variable,Double> addVariable(Variable variable, Double coefficient){
        this.coefficientByVariable.put(variable,coefficient);
        return this.coefficientByVariable;
    }

}
