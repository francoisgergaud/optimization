package fgd.optimization.linearp.model.lexical;

import fgd.optimization.linearp.model.Variable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class Solution {

    /**
     * The objective-function value;
     */
    Double zValue;

    /**
     * The variable values. These values applied to the original objective-function gives the zValue.
     */
    Map<Variable, Double> variableValues;
}
